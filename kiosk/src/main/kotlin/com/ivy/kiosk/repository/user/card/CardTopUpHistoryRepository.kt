package com.ivy.kiosk.repository.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CardTopUpHistoryRepository : JpaRepository<CardTopUpHistoryEntity, Long> {
}