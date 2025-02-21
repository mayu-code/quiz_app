package com.app.quiz_app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ActiveQuestion {

    @Id
    private Long id = 1L;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    private LocalDateTime startTime;

    private int durationInSeconds;

    private boolean isSuspend;

    private boolean isFinished;
}
