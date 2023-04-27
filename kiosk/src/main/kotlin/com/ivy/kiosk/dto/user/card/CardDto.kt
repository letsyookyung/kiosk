package com.ivy.kiosk.dto.user.card

import java.time.LocalDate

data class CardDto(
    val id: Long? = 0,
    val userId: Long?,
    val cardNumber: String?,
    val createdAt: LocalDate?,
    val balance: Int? = 0
    )
