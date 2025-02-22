package com.app.quiz_app.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quiz_app.dto.response.DataResponse;
import com.app.quiz_app.dto.response.MessageResponse;
import com.app.quiz_app.entities.ActiveQuestion;
import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.repository.ActiveQuestionRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "https://quizbyayush.netlify.app" })
public class UserController {

    @Autowired
    private ActiveQuestionRepository activeQuestionRepository;

    @GetMapping("/question")
    public ResponseEntity<?> getActiveQuestion() {
        Optional<ActiveQuestion> activeQuestion = activeQuestionRepository.findById(1L);

        DataResponse res = new DataResponse();

        if (activeQuestion.isEmpty()) {
            res.setMessage("No Active Question");
            res.setStatus(HttpStatus.NOT_FOUND);
            res.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        res.setMessage("Active Question Retrieved");
        res.setStatus(HttpStatus.OK);
        res.setStatusCode(200);
        res.setData(activeQuestion.get());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/timer")
    public ResponseEntity<?> getRemainingTime() {
        Optional<ActiveQuestion> activeQuestion = activeQuestionRepository.findById(1L);

        DataResponse res = new DataResponse();

        if (activeQuestion.isEmpty()) {
            res.setMessage("No Active Question");
            res.setStatus(HttpStatus.NOT_FOUND);
            res.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        LocalDateTime startTime = activeQuestion.get().getStartTime();
        int duration = activeQuestion.get().getDurationInSeconds();
        long elapsedTime = ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
        long remainingTime = duration - elapsedTime;

        if (remainingTime <= 0) {
            ActiveQuestion updatedQuestion = activeQuestion.get();
            updatedQuestion.setFinished(true);
            this.activeQuestionRepository.save(updatedQuestion);
            // this.activeQuestionRepository.delete(activeQuestion.get());
            res.setMessage("Time Left");
            res.setStatus(HttpStatus.OK);
            res.setStatusCode(200);
            res.setData(0);
            return ResponseEntity.ok(res);
        }

        res.setMessage("Time Left");
        res.setStatus(HttpStatus.OK);
        res.setStatusCode(200);
        res.setData(remainingTime);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/deleteQuestion")
    public ResponseEntity<?> deleteQuestion() {

        MessageResponse res = new MessageResponse();

        Optional<ActiveQuestion> activeQuestion = this.activeQuestionRepository.findById(1L);

        if (activeQuestion.isEmpty()) {
            res.setMessage("No Active Question");
            res.setStatus(HttpStatus.NOT_FOUND);
            res.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        try {
            activeQuestionRepository.delete(activeQuestion.get());
            res.setMessage("Question Deleted");
            res.setStatus(HttpStatus.OK);
            res.setStatusCode(200);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            res.setMessage(e.getMessage());
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setStatusCode(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

}
