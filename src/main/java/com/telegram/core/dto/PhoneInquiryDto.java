package com.telegram.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneInquiryDto {
    private Long id;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String usernames;
    private String status;
    private String isPremium;
}
