package com.ivy.kiosk.dto.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesEntity

data class MovieShowtimesWithSeatsDto (
    val movieShowtimes: MovieShowtimesEntity,
    val availableSeats: List<String?>
)