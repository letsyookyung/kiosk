package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.repository.movie.MovieShowtimesRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MovieShowtimesEntityService(
    private val movieShowtimesRepository: MovieShowtimesRepository,
) {

    fun addDailyShowtimes(movieShowtimesEntityList: List<MovieShowtimesEntity>): List<MovieShowtimesEntity> {
        return movieShowtimesEntityList.map { movieShowtimesEntity ->
            movieShowtimesRepository.save(movieShowtimesEntity) }
    }

    fun findByDate(today: LocalDate): List<MovieShowtimesEntity> {
        return movieShowtimesRepository.findByDate(today)
    }

}