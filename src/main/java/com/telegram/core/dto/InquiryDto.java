package com.telegram.core.dto;

import com.telegram.core.InquiryStatus;
import lombok.Data;

import java.util.Date;


public class InquiryDto {
    private Long id;
    private InquiryStatus status;
    private Long duration;
    private Date createdAt;

    public InquiryDto(Long id, InquiryStatus status, Long duration, Date createdAt) {
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InquiryStatus getStatus() {
        return status;
    }

    public void setStatus(InquiryStatus status) {
        this.status = status;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
