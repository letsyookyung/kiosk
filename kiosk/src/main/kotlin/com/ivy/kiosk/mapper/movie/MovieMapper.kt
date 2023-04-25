package com.ivy.kiosk.mapper.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.model.movie.MovieRequestModel
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component


@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class MovieMapper {

    abstract fun toDto(model: MovieRequestModel): MovieDto

    abstract fun toEntity(dto: MovieDto): MovieEntity
}