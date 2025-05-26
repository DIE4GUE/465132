package com.example.english_learning.controller;

import com.example.english_learning.dto.CardDto;
import com.example.english_learning.dto.CheckAnswerResponseDto;
import com.example.english_learning.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Void> createCard(@RequestBody CardDto card) {
        cardService.saveNewCard(
                card.getQuestion(),
                card.getAnswers(),
                card.getCardType(),
                card.getExample()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/random")
    public ResponseEntity<CardDto> getRandomCard() {
        CardDto card = cardService.getRandomCard();
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<CheckAnswerResponseDto> checkCard(@PathVariable("id") UUID id, @RequestParam("answer") String answer) {
        return ResponseEntity.ok(cardService.checkAnswer(id, answer));
    }

    @PutMapping()
    public ResponseEntity<CardDto> updateCard(@RequestBody CardDto card) {
        return ResponseEntity.ok(cardService.update(card));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable("id") UUID id) {
        cardService.deleteCard(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
