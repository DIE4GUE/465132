package com.example.english_learning.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class CheckAnswerResponseDto {
    private boolean isCorrect;
    private Set<String> newAchievements;
}
