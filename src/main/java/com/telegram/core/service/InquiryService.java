// AuthService.java
package com.telegram.core.service;

import com.telegram.core.InquiryStatus;
import com.telegram.core.config.TdlibClient;
import com.telegram.core.dto.InquiryDto;
import com.telegram.core.entity.Inquiry;
import com.telegram.core.entity.InquiryDetail;
import com.telegram.core.repository.InquiryDetailRepository;
import com.telegram.core.repository.InquiryRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InquiryService {
    private final Client client; // tdlib client
    private final InquiryRepository inquiryRepository;
    private final InquiryDetailRepository inquiryDetailRepository;
    private final UserService userService;

    public InquiryService(TdlibClient tdlibClient, InquiryRepository inquiryRepository, InquiryDetailRepository inquiryDetailRepository, UserService userService) {
        this.client = tdlibClient.getClient();
        this.inquiryRepository = inquiryRepository;
        this.inquiryDetailRepository = inquiryDetailRepository;
        this.userService = userService;
    }

    public Page<InquiryDto> searchInquiries(List<InquiryStatus> statuses, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (statuses == null || statuses.isEmpty()) {
            statuses = new ArrayList<>();
            statuses.add(InquiryStatus.SAVED);
            statuses.add(InquiryStatus.FAILED);
            statuses.add(InquiryStatus.STARTED);
            statuses.add(InquiryStatus.FINISHED);
        }
        return inquiryRepository.findByStatusIn(statuses, pageable);
    }

    public List<InquiryDetail> readDetailExcelFile(MultipartFile file, Inquiry inquiry) throws IOException {

        List<InquiryDetail> inquiryDetails = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                InquiryDetail inquiryDetail = new InquiryDetail();
                inquiryDetail.setPhoneNumber(String.valueOf(BigDecimal.valueOf(row.getCell(0).getNumericCellValue()).toPlainString()));
                inquiryDetail.setInquiry(inquiry);
                inquiryDetails.add(inquiryDetail);
            }
        }
        return inquiryDetails;
    }

    public Inquiry save(MultipartFile file) throws IOException {
        Inquiry newInquiry = new Inquiry();
        Long startTime = System.nanoTime();
        newInquiry = inquiryRepository.save(newInquiry);
        List<InquiryDetail> inquiryDetails = readDetailExcelFile(file, newInquiry);
        inquiryDetails = inquiryDetailRepository.saveAll(inquiryDetails);
        Long endTime = System.nanoTime();
        newInquiry.setDuration((endTime - startTime) / 1000000L);
        newInquiry.setInquiryDetails(inquiryDetails);
        newInquiry.setStatus(InquiryStatus.SAVED);
        newInquiry = inquiryRepository.save(newInquiry);
        if (newInquiry.getId() != null) {
            return newInquiry;
        }
        return null;
    }

    public void startInquiry(Long inquiryId) throws InterruptedException {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).get();
        if (inquiry.getInquiryDetails() == null) {
            throw new RuntimeException("inquiry details is null");
        }
        inquiry.setStatus(InquiryStatus.STARTED);
        inquiryRepository.save(inquiry);
        int batchSize = 30;
        List<InquiryDetail> newInquiryDetails = inquiry.getInquiryDetails().stream().filter(inquiryDetail -> inquiryDetail.getUserId()==null).toList();
        int total = newInquiryDetails.size();
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<InquiryDetail> batch = newInquiryDetails.subList(i, end);
            List<InquiryDetail> results = importAndGetUsersInBatches(batch);
            inquiryDetailRepository.saveAll(results);
            int delay = ThreadLocalRandom.current().nextInt(180000, 580000); // بین 60ms تا 300ms
            Thread.sleep(delay);
//            System.out.println("Processed batch " + (i / batchSize + 1) + " with delay " + delay + " ms");
        }
        inquiry.setStatus(InquiryStatus.FINISHED);
        inquiryRepository.save(inquiry);
    }

    public InquiryStatus getInquiryStatus(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId).get();
        if (inquiry.getInquiryDetails() == null) {
            throw new RuntimeException("inquiry details is null");
        } else {
            return inquiry.getStatus();
        }
    }


    public List<InquiryDetail> importAndGetUsersInBatches(List<InquiryDetail> inquiryDetails) throws InterruptedException {
        List<InquiryDetail> result = new ArrayList<>();

        int batchSize = 15;
        for (int i = 0; i < inquiryDetails.size(); i += batchSize) {
            List<InquiryDetail> batch = inquiryDetails.subList(i, Math.min(i + batchSize, inquiryDetails.size()));
            System.out.println("Processing batch " + ((i / batchSize) + 1) + " with " + batch.size() + " numbers...");

            processBatch(batch, result);

            // Wait between batchs
            int sleep = 14000 + new Random().nextInt(5000); // 5000 - 10000 ms
            System.out.println("Sleeping " + sleep + "ms before next batch...");
            Thread.sleep(sleep);
        }

        return result;
    }

    private void processBatch(List<InquiryDetail> batch, List<InquiryDetail> result) throws InterruptedException {
        TdApi.Contact[] contacts = batch.stream()
                .map(detail -> new TdApi.Contact(
                        detail.getPhoneNumber(),
                        "",
                        "",
                        "",
                        0
                ))
                .toArray(TdApi.Contact[]::new);

        CountDownLatch latch = new CountDownLatch(1);

        client.send(new TdApi.ImportContacts(contacts), object -> {
            if (object instanceof TdApi.ImportedContacts) {
                TdApi.ImportedContacts imported = (TdApi.ImportedContacts) object;

                for (int i = 0; i < imported.userIds.length; i++) {
                    long userId = imported.userIds[i];
                    InquiryDetail detail = batch.get(i);
                    detail.setUserId(userId);
                    result.add(detail);
                }
            } else if (object instanceof TdApi.Error) {
                TdApi.Error error = (TdApi.Error) object;
                if (error.code == 429) {
                    inquiryDetailRepository.saveAll(result);
                    int retryAfter = Integer.parseInt(error.message.replaceAll("\\D+", ""));
                    System.err.println("Rate limit hit! Sleeping for " + retryAfter + "s...");
                    try {
                        Thread.sleep(retryAfter * 10L);
                        try {
                            processBatch(batch, result);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Error importing contacts: " + error.message);
                }
            }
            latch.countDown();
        });

        latch.await();

        //Random delay per PhoneNumber
        for (int i = 0; i < batch.size(); i++) {
            int delay = 100 + new Random().nextInt(400); // 100–500ms
            Thread.sleep(delay);
        }
    }
}