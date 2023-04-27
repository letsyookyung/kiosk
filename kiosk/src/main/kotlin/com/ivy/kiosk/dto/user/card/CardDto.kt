package com.ivy.kiosk.dto.user.card

import java.time.LocalDateTime

data class CardDto(
    val id: Long? = 0,
    val userId: Long,
    val cardNumber: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val balance: Int? = 0,
)
