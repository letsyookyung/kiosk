package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<MovieEntity, Long> {
}