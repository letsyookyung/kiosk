package com.ivy.kiosk.repository.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface CardTopUpHistoryRepository : JpaRepository<CardTopUpHistoryEntity, Long> {

    @Query("SELECT SUM(c.amount) FROM CardTopUpHistoryEntity c WHERE c.createdAt BETWEEN :start AND :end")
    fun getTotalTopUpAmountByDate(start: LocalDateTime, end: LocalDateTime ): Int
}