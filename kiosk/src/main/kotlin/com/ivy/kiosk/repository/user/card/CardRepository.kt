package com.ivy.kiosk.repository.user.card

import com.ivy.kiosk.dao.user.card.CardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CardRepository : JpaRepository<CardEntity, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE CardEntity c SET c.balance = c.balance + :amount WHERE c.cardNumber = :cardNumber")
    fun updateBalance(cardNumber: String, amount: Int): Int

}