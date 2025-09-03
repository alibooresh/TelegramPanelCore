package com.telegram.core.dto;

import lombok.Data;


public class InquiryDetailDto {
    private Long id;
    private Long userId;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String usernames;
    private String inquiryStatus;

    public InquiryDetailDto(Long id, Long userId, String phoneNumber, String firstName, String lastName, String usernames, String inquiryStatus) {
        this.id = id;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.usernames = usernames;
        this.inquiryStatus = inquiryStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsernames() {
        return usernames;
    }

    public void setUsernames(String usernames) {
        this.usernames = usernames;
    }

    public String getInquiryStatus() {
        return inquiryStatus;
    }

    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }
}
