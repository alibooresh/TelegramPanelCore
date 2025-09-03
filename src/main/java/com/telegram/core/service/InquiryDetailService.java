package com.telegram.core.service;

import com.telegram.core.dto.InquiryDetailDto;
import com.telegram.core.entity.Inquiry;
import com.telegram.core.entity.InquiryDetail;
import com.telegram.core.repository.InquiryDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryDetailService {
    private final InquiryDetailRepository inquiryDetailRepository;

    public InquiryDetailService(InquiryDetailRepository inquiryDetailRepository) {
        this.inquiryDetailRepository = inquiryDetailRepository;
    }

    public InquiryDetail findById(Long id) {
        return inquiryDetailRepository.findById(id).get();
    }

    public List<InquiryDetail> findAllByInquiryId(Long inquiryId) {
        return inquiryDetailRepository.findByInquiryId(inquiryId);
    }

    public InquiryDetail fillData(Long id) {
        return null;
    }

    public Page<InquiryDetailDto> searchDetails(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Inquiry inquiry = new Inquiry();
        inquiry.setId(id);
        return inquiryDetailRepository.findByInquiry(inquiry, pageable);
    }
}
