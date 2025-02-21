package com.app.quiz_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.quiz_app.entities.ActiveQuestion;

@Repository
public interface ActiveQuestionRepository extends JpaRepository<ActiveQuestion, Long> {

}
