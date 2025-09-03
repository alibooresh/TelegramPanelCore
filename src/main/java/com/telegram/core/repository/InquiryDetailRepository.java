package com.telegram.core.repository;

import com.telegram.core.dto.InquiryDetailDto;
import com.telegram.core.entity.Inquiry;
import com.telegram.core.entity.InquiryDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryDetailRepository extends JpaRepository<InquiryDetail, Long> {

    public List<InquiryDetail> findByInquiryId(Long inquiryId);


    @Query("SELECT new com.telegram.core.dto.InquiryDetailDto(d.id, d.userId, d.phoneNumber, d.firstName, d.lastName, d.usernames, d.inquiryStatus) " +
            "FROM InquiryDetail d WHERE d.inquiry = :inquiry")
    Page<InquiryDetailDto> findByInquiry(@Param("inquiry")Inquiry inquiry, Pageable pageable);

}


