package com.example.english_learning.service;

import com.example.english_learning.dto.CardDto;
import com.example.english_learning.dto.CheckAnswerResponseDto;
import com.example.english_learning.model.Answer;
import com.example.english_learning.model.Card;
import com.example.english_learning.model.CardType;
import com.example.english_learning.model.Customer;
import com.example.english_learning.repository.AnswerRepo;
import com.example.english_learning.repository.CardRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CardService {
    private static final int THRESHOLD = 5; // TODO вынести в настройки

    private final CardRepo cardRepo;
    private final AnswerRepo answerRepo;
    private final UserService userService;

    @Transactional
    public void saveNewCard(String question, Set<String> answer, CardType type, String example) {
        var currentCustomer = userService.getCurrentCustomer();

        var card = new Card()
                .setQuestion(question)
                .setCardType(type)
                .setExampleOfUsage(example)
                .setCustomer(currentCustomer);

        cardRepo.save(card);

        var answers = answer.stream()
                .map(a -> new Answer()
                        .setAnswerText(a.trim())
                        .setCard(card)
                )
                .collect(Collectors.toSet());

        card.getAnswers().addAll(answers);

        answerRepo.saveAll(answers);
    }

    public CardDto getRandomCard() {
        Customer customer = userService.getCurrentCustomer();

        var cards = cardRepo.findRandomCardWithWeightedChance(
                THRESHOLD,
                customer.getId(),
                PageRequest.of(0, 1)
        );

        if (isEmpty(cards)) throw new EntityNotFoundException("Current customer has no cards");

        var card = cards.get(0);

        card.setLastShownDateTime(LocalDateTime.now());
        card.setShowCount(card.getShowCount() + 1);
        cardRepo.save(card);

        return new CardDto()
                .setCardId(card.getId())
                .setCardType(card.getCardType())
                .setQuestion(card.getQuestion())
                .setExample(card.getExampleOfUsage())
                .setAnswers(
                        card.getAnswers().stream()
                                .map(Answer::getAnswerText)
                                .collect(Collectors.toSet())
                );
    }

    @Transactional
    public CardDto update(CardDto cardDto) {
        var customer = userService.getCurrentCustomer();
        var oldCard = cardRepo.findCardByIdAndCustomer(cardDto.getCardId(), customer);

        if (oldCard.isEmpty()) throw new EntityNotFoundException("Card with id " + oldCard + " not found");

        var answers = oldCard.get().getAnswers();
        answerRepo.deleteAll(answers);
        answers.clear();

        cardDto.getAnswers().forEach(a -> {
            var newAnswer = new Answer();
            newAnswer.setAnswerText(a.trim());
            newAnswer.setCard(oldCard.get());
            answerRepo.save(newAnswer);
            answers.add(newAnswer);
        });

        oldCard.get().setAnswers(answers);
        oldCard.get().setQuestion(cardDto.getQuestion());
        oldCard.get().setExampleOfUsage(cardDto.getExample());

        var newCard = cardRepo.save(oldCard.get());

        return new CardDto()
                .setCardId(newCard.getId())
                .setCardType(newCard.getCardType())
                .setQuestion(newCard.getQuestion())
                .setExample(newCard.getExampleOfUsage())
                .setAnswers(
                        newCard.getAnswers().stream()
                                .map(Answer::getAnswerText)
                                .collect(Collectors.toSet())
                );
    }

    @Transactional()
    public CheckAnswerResponseDto checkAnswer(UUID cardId, String answer) {
        var customer = userService.getCurrentCustomer();
        var card = cardRepo.findCardByIdAndCustomer(cardId, customer);

        if (card.isEmpty()) throw new EntityNotFoundException("Card with id " + cardId + " not found");

        var response = new CheckAnswerResponseDto();
        var isRight = card.get().getAnswers().stream()
                .anyMatch(a -> a.getAnswerText().equals(answer));

        if (isRight) {
            response.setCorrect(true);
            var updatebleCard = card.get();
            updatebleCard.setCorrectAnswerCount(updatebleCard.getCorrectAnswerCount() + 1);
            cardRepo.save(updatebleCard);
        }

        return response;
    }

    @Transactional
    public void deleteCard(UUID cardId) {
        var customer = userService.getCurrentCustomer();
        var card = cardRepo.findCardByIdAndCustomer(cardId, customer);
        if (card.isEmpty()) throw new EntityNotFoundException("Card with id " + cardId + " not found");
        cardRepo.deleteById(cardId);
    }
}
