package com.ivy.kiosk.service.movie


import com.ivy.kiosk.dao.movie.SeatsEntity
import com.ivy.kiosk.repository.movie.SeatsRepository
import org.springframework.stereotype.Service

@Service
class SeatsEntityService(private val seatsRepository: SeatsRepository){

    fun findSeatsByShowtimesId(id: Long): List<String?> {
        return seatsRepository.findSeatsByShowtimesId(id).map { it.seatNumber }

    }
}