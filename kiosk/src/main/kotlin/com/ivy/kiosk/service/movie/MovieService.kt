package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.*
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class MovieService(
    private val movieMapper: MovieMapper,
    private val movieEntityService: MovieEntityService,
) {
    fun add(movieDtoList: List<MovieDto>): List<MovieDto> {
        val movieEntityList = movieDtoList.map { movie -> movieMapper.toEntity(movie) }
        return movieEntityService.add(movieEntityList).map { movieEntity -> movieMapper.toDto(movieEntity) }
    }

    fun findByBetweenDate(date: LocalDate): List<MovieDto>? {
        return movieEntityService.findByBetweenDate(date)?.map { movieEntity -> movieMapper.toDto(movieEntity) } ?: null
    }


}

