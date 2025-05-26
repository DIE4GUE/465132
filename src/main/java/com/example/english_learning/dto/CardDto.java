package com.example.english_learning.dto;

import com.example.english_learning.model.CardType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
public class CardDto {
    private UUID cardId;
    private String question;
    private Set<String> answers;
    private CardType cardType;
    private String example;
}
