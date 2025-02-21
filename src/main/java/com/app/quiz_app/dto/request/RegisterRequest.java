package com.app.quiz_app.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
