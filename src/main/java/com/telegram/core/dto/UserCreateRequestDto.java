package com.telegram.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long mobile;
    private float income;
    private String gender;
}
