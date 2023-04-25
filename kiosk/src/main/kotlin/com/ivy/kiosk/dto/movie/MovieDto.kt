package com.ivy.kiosk.dto.movie

import java.time.LocalDate

data class MovieDto(
    val id: Long? = 0,
    val title: String,
    val runningTime: Int? = 0,
    val releaseDate: LocalDate?,
    val endDate: LocalDate?,
    val price: Int? = 12000,
    )
