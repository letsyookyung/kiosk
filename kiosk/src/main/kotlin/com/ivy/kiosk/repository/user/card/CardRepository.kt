package com.ivy.kiosk.repository.user.card

import com.ivy.kiosk.dao.user.card.CardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : JpaRepository<CardEntity, Long> {
}