package com.example.UserLogin.payload;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String token;
}
