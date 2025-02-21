package com.app.quiz_app.dto.request;

import lombok.Data;

@Data
public class StartRequest {
    private String content;
    private int duration;
}
