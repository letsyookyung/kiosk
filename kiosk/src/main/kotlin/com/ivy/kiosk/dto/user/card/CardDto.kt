package com.ivy.kiosk.dto.user.card

import java.time.LocalDate

data class CardDto(
    val id: Long? = 0,
    val cardNumber: String? = null,
    val createdAt: LocalDate = LocalDate.now(),
    val balance: Int? = 0
    )
