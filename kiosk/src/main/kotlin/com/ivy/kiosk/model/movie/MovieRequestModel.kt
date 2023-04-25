package com.ivy.kiosk.model.movie

import java.time.LocalDate

data class MovieRequestModel(
    val title: String,
    val runningTime: Int,
    val releaseDate: LocalDate,
    val endDate: LocalDate,
    val price: Int,
)
