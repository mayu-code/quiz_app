package com.app.quiz_app.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quiz_app.dto.request.StartRequest;
import com.app.quiz_app.dto.response.DataResponse;
import com.app.quiz_app.dto.response.MessageResponse;
import com.app.quiz_app.entities.ActiveQuestion;
import com.app.quiz_app.entities.Admin;
import com.app.quiz_app.repository.ActiveQuestionRepository;
import com.app.quiz_app.service.impl.AdminServiceImpl;

@RestController
@RequestMapping("/api/admin")
// @PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174", "https://quizbyayush.netlify.app" })
public class QuestionController {

    @Autowired
    private ActiveQuestionRepository activeQuestionRepository;

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @PostMapping("/start")
    public ResponseEntity<?> startQuiz(@RequestHeader("Authorization") String jwt, @RequestBody StartRequest request) {

        Optional<Admin> admin = this.adminServiceImpl.getAdminByJwt(jwt);

        MessageResponse res = new MessageResponse();

        if (!admin.isPresent()) {
            res.setMessage("invalid token");
            res.setStatus(HttpStatus.UNAUTHORIZED);
            res.setStatusCode(401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);

        }

        Optional<ActiveQuestion> question = this.activeQuestionRepository.findById(1L);

        if (question.isPresent() && !question.get().isFinished()) {
            res.setMessage("Quiz already started");
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.setStatusCode(400);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        try {
            ActiveQuestion activeQuestion = new ActiveQuestion();
            activeQuestion.setContent(request.getContent());
            activeQuestion.setStartTime(LocalDateTime.now());
            activeQuestion.setDurationInSeconds(request.getDuration());
            activeQuestionRepository.save(activeQuestion);
            res.setMessage("Quiz Started");
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

    @PostMapping("/suspend")
    public ResponseEntity<?> suspendQuestion(@RequestHeader("Authorization") String jwt) {

        Optional<Admin> admin = this.adminServiceImpl.getAdminByJwt(jwt);

        MessageResponse res = new MessageResponse();

        if (!admin.isPresent()) {
            res.setMessage("invalid token");
            res.setStatus(HttpStatus.UNAUTHORIZED);
            res.setStatusCode(401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);

        }
        Optional<ActiveQuestion> activeQuestion = this.activeQuestionRepository.findById(1L);

        if (activeQuestion.isEmpty()) {
            res.setMessage("No Active Question");
            res.setStatus(HttpStatus.NOT_FOUND);
            res.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        try {
            ActiveQuestion updatedQuestion = activeQuestion.get();

            if (updatedQuestion.isSuspend()) {
                res.setMessage("No Active Question");
                res.setStatus(HttpStatus.BAD_REQUEST);
                res.setStatusCode(400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }

            updatedQuestion.setSuspend(true);
            activeQuestionRepository.save(updatedQuestion);
            res.setMessage("Question is Stopped");
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

    @GetMapping("/getAdmin")
    public ResponseEntity<?> getAdmin(@RequestHeader("Authorization") String jwt) {
        Optional<Admin> admin = this.adminServiceImpl.getAdminByJwt(jwt);

        DataResponse res = new DataResponse();

        if (!admin.isPresent()) {
            res.setMessage("invalid token");
            res.setStatus(HttpStatus.UNAUTHORIZED);
            res.setStatusCode(401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);

        }

        res.setMessage("Admin Fetch Successfully");
        res.setStatus(HttpStatus.OK);
        res.setStatusCode(200);
        res.setData(admin.get());

        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

}
