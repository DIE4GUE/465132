package com.example.english_learning.repository;

import com.example.english_learning.model.Card;
import com.example.english_learning.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepo extends JpaRepository<Card, UUID> {

    @Query(value = """
            SELECT * FROM card
            WHERE correct_answer_count < :threshold
            AND customer_id = :customer_id
            ORDER BY random() / (correct_answer_count + 1)
            LIMIT :#{#pageable.pageSize}
            """, nativeQuery = true)
    List<Card> findRandomCardWithWeightedChance(
            @Param("threshold") int threshold, @Param("customer_id") Long customerId, Pageable pageable
    );

    Optional<Card> findCardByIdAndCustomer(UUID id, Customer customerId);


    @Query(value = "SELECT count(c) FROM Card c WHERE c.correctAnswerCount < :count")
    long countActualCard(int count);

    boolean existsCardByQuestion(String question);
}
