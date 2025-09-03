
// AuthController.java
package com.telegram.core.controller;

import com.telegram.core.InquiryStatus;
import com.telegram.core.dto.InquiryDto;
import com.telegram.core.dto.InquiryRequestDto;
import com.telegram.core.entity.Inquiry;
import com.telegram.core.service.InquiryService;
import com.telegram.core.specification.inquiry.InquirySearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            Inquiry inquiry = inquiryService.save(file);
            return ResponseEntity.ok("Uploaded and saved " + inquiry.getInquiryDetails().size() + " records with request id " + inquiry.getId() + ".");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public Page<InquiryDto> searchInquiries(
            @RequestParam(required = false) List<InquiryStatus> statuses,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return inquiryService.searchInquiries(statuses, page, size);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startInquiry(@RequestBody InquiryRequestDto inquiryRequestDto) throws InterruptedException {
        inquiryService.startInquiry(inquiryRequestDto.getInquiryId());
        return ResponseEntity.ok("Start inquiry");
    }

    @PostMapping("/getInquiryStatus")
    public ResponseEntity<InquiryStatus> getInquiryStatus(@RequestBody InquiryRequestDto inquiryRequestDto) throws InterruptedException {
        return ResponseEntity.ok(inquiryService.getInquiryStatus(inquiryRequestDto.getInquiryId()));
    }
}
