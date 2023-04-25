package com.ivy.kiosk.mapper.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.model.movie.MovieRequestModel
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class MovieMapper {

    abstract fun toDto(model: MovieRequestModel): MovieDto

    abstract fun toDto(date: LocalDate, entity: MovieEntity): MovieShowtimesDto

    abstract fun toEntity(dto: MovieDto): MovieEntity

    abstract fun toEntity(dto: MovieShowtimesDto): MovieShowtimesEntity

    fun createShowtimesDto(runningMovieList: List<MovieEntity>, date: LocalDate): List<MovieShowtimesDto> {
        return runningMovieList.map { movie -> toDto(date, movie) }
    }

}