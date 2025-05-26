package com.example.english_learning.repository;

import com.example.english_learning.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, UUID> {
}
