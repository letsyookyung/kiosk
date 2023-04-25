package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface MovieShowtimesRepository : JpaRepository<MovieShowtimesEntity, Long> {

    fun findByDate(today: LocalDate): List<MovieShowtimesEntity>

}