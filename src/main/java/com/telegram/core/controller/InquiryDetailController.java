
// AuthController.java
package com.telegram.core.controller;

import com.telegram.core.InquiryStatus;
import com.telegram.core.dto.InquiryDetailDto;
import com.telegram.core.dto.InquiryRequestDto;
import com.telegram.core.entity.Inquiry;
import com.telegram.core.entity.InquiryDetail;
import com.telegram.core.service.InquiryDetailService;
import com.telegram.core.service.InquiryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/inquiryDetail")
public class InquiryDetailController {

    private final InquiryDetailService inquiryDetailService;

    public InquiryDetailController(InquiryDetailService inquiryDetailService) {
        this.inquiryDetailService = inquiryDetailService;
    }



    @GetMapping("/search")
    public Page<InquiryDetailDto> searchDetails(
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return inquiryDetailService.searchDetails(id, page, size);
    }

}
