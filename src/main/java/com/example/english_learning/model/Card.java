package com.example.english_learning.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {"answers"})
@Accessors(chain = true)
public class Card {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            strategy = "org.hibernate.id.UUIDGenerator",
            name = "UUID"
    )
    private UUID id;
    private String question;
    private int showCount;
    private int correctAnswerCount;
    private LocalDateTime lastShownDateTime;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "card", cascade = CascadeType.REMOVE)
    private Set<Answer> answers = new HashSet<>();
    @Enumerated(value = EnumType.STRING)
    private CardType cardType;
    private String exampleOfUsage;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
}
