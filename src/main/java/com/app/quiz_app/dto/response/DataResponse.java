package com.app.quiz_app.dto.response;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class DataResponse {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private Object data;
}
