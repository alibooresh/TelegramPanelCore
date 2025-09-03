package com.telegram.core.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inquiryDetail")
public class InquiryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String usernames;
    private String status;
    private Boolean isPremium;
    private String inquiryStatus;
    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;
}
