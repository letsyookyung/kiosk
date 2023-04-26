package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.SeatsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeatsRepository : JpaRepository<SeatsEntity, Long> {

    fun findSeatsByShowtimesId(id: Long): List<SeatsEntity>

    fun findByShowtimesIdAndSeatNumber(showtimesId: Long, seatNumber: String): SeatsEntity?

}