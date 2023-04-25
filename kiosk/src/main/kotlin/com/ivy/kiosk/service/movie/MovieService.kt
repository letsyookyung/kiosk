package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service


@Service
class MovieService(
    private val movieMapper: MovieMapper,
    private val movieEntityService: MovieEntityService,
) {

    fun add(movieDtoList: List<MovieDto>): List<MovieEntity> {
        val movieEntityList = movieDtoList.map {movie -> movieMapper.toEntity(movie) }
        return movieEntityService.add(movieEntityList)
    }


}