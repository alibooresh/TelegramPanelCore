package com.telegram.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private long mobile;
    private float income;
    private String gender;
}
