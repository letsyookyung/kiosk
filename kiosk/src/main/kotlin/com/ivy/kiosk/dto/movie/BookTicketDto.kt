package com.ivy.kiosk.dto.movie

data class BookTicketDto(
    val cardNumber: String,
    val showtimesId: Long,
    val price: Int,
    val seatNumber: String,
)
