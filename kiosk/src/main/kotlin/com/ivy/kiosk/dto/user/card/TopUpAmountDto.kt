package com.ivy.kiosk.dto.user.card

import java.time.LocalDateTime


data class TopUpAmountDto (
    val id: Long? = 0,
    val cardNumber: String,
    val amount: Int? = 0,
) {
    init {
        val createdAt = LocalDateTime.now()
    }
}
