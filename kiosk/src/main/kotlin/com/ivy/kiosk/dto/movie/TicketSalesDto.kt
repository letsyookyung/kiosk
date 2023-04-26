package com.ivy.kiosk.dto.movie

import java.time.LocalDateTime

data class TicketSalesDto (
    val showtimesId: Long,
    val cardNumber: String?,
    val date: LocalDateTime? = LocalDateTime.now(),
)