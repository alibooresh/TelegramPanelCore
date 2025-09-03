package com.telegram.core.entity;

import com.telegram.core.InquiryStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "inquiry")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InquiryDetail> inquiryDetails;
    @Enumerated(EnumType.STRING)
    @Column
    private InquiryStatus status;
    @Column
    private Long duration;
    @Column
    private Date createdAt = new Date();
}

