package com.example.WebLearn.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String phone;
    private String sex;
    private String address;
    private String email;
    private String password;
    private String avatarUrl;
    private String role;
}
