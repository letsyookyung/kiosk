package com.ivy.kiosk.dto.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesType
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

data class MovieShowtimesDto(
    val id: Long? = 0,
    val date: LocalDate,
    val title: String,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val runningTime: Long?,
    var type: MovieShowtimesType?,
    var price: Int?,
) {
    init {
        if (startTime?.isBefore(LocalTime.of(12, 0)) == true) {
            type = MovieShowtimesType.MATINEE
            price = (price?.times(0.7))?.roundToInt()
        }

    }
}


