package com.telegram.core.dto;

import com.telegram.core.InquiryStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ProxyDto {
    private String server;
    private int port ;
    private String secret ;

}
