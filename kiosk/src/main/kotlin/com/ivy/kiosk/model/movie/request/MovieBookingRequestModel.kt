package com.ivy.kiosk.model.movie.request

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.validation.constraints.Pattern

data class MovieBookingRequestModel(

    val cardNumber: String,

    val password: String,

    var date: LocalDate,

    val title: String,

    val seatNumber: String,

    val startTime: String,
)
