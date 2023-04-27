package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.time.LocalTime

interface MovieShowtimesRepository : JpaRepository<MovieShowtimesEntity, Long> {

    fun findByDate(today: LocalDate): List<MovieShowtimesEntity>?

    fun findByDateAndTitleAndStartTime(date: LocalDate, title: String, startTime: LocalTime?): MovieShowtimesEntity?

}