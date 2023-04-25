package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.repository.movie.MovieRepository
import org.springframework.stereotype.Service


@Service
class MovieEntityService(
    private val movieRepository: MovieRepository,
) {

    fun add(movieEntityList: List<MovieEntity>): List<MovieEntity> {
        return movieEntityList.map { movieEntity -> movieRepository.save(movieEntity) }
    }

}