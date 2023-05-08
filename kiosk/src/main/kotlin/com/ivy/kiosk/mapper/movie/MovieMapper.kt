package com.ivy.kiosk.mapper.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.dao.movie.SeatsEntity
import com.ivy.kiosk.dao.movie.TicketSalesEntity
import com.ivy.kiosk.dto.movie.SeatsDto
import com.ivy.kiosk.dto.movie.TicketSalesDto
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.model.movie.request.MovieBookingRequestModel
import com.ivy.kiosk.model.movie.request.MovieRequestModel
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class MovieMapper {

    abstract fun toDto(model: MovieRequestModel): MovieDto

    abstract fun toDto(date: LocalDate, dto: MovieDto): MovieShowtimesDto

    abstract fun toDto(model: MovieBookingRequestModel): MovieShowtimesDto

    abstract fun toDto(showtimesId: Long, cardNumber: String): TicketSalesDto

    abstract fun toDto(movieEntity: MovieEntity): MovieDto

    abstract fun toDto(movieShowtimesEntity: MovieShowtimesEntity): MovieShowtimesDto

    abstract fun toDto(ticketSalesEntity: TicketSalesEntity): TicketSalesDto

    abstract fun toEntity(dto: MovieDto): MovieEntity

    abstract fun toEntity(dto: MovieShowtimesDto): MovieShowtimesEntity

    abstract fun toEntity(dto: TicketSalesDto): TicketSalesEntity

    abstract fun toEntity(dto: SeatsDto): SeatsEntity
    fun toDto(runningMovieList: List<MovieDto>, date: LocalDate): List<MovieShowtimesDto> {
        return runningMovieList.map { movie -> toDto(date, movie) }
    }

}

