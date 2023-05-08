package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.SeatsEntity
import com.ivy.kiosk.dto.movie.SeatsDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service

@Service
class SeatsService(
    private val movieMapper: MovieMapper,
    private val seatsEntityService: SeatsEntityService,
) {

    fun addToSeatsHistory(seatsDto: SeatsDto): SeatsEntity {
        val seatsEntity = movieMapper.toEntity(seatsDto)
        return seatsEntityService.add(seatsEntity)
    }

    fun isAlreadyTakenSeat(showtimesId: Long, seatNumber: String): Boolean {
        return seatsEntityService.findByShowtimesIdAndSeatNumber(showtimesId, seatNumber) != null
    }

}