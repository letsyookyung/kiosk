package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface MovieRepository : JpaRepository<MovieEntity, Long> {

    @Query("SELECT m FROM MovieEntity m WHERE m.releaseDate <= :date AND m.endDate >= :date")
    fun findByBetweenDate(date: LocalDate): List<MovieEntity>?

}