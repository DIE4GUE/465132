package com.example.english_learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class Answer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            strategy = "org.hibernate.id.UUIDGenerator",
            name = "UUID"
    )
    private UUID id;
    private String answerText;
    @ManyToOne
    private Card card;
}
