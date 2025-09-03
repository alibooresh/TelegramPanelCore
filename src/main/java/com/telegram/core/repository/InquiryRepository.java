package com.telegram.core.repository;

import com.telegram.core.InquiryStatus;
import com.telegram.core.dto.InquiryDetailDto;
import com.telegram.core.dto.InquiryDto;
import com.telegram.core.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT new com.telegram.core.dto.InquiryDto(d.id,d.status,d.duration,d.createdAt) " +
            "FROM Inquiry d WHERE d.status in :statuses")
    Page<InquiryDto> findByStatusIn(@Param("statuses") List<InquiryStatus> statuses, Pageable pageable);}

