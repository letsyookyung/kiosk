package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.*
import com.ivy.kiosk.dto.movie.SeatsDto
import com.ivy.kiosk.dto.movie.TicketSalesDto
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.dto.movie.MovieShowtimesWithSeatsDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class MovieService(
    private val movieMapper: MovieMapper,
    private val movieEntityService: MovieEntityService,
    private val movieShowtimesEntityService: MovieShowtimesEntityService,
    private val seatsEntityService: SeatsEntityService,
    private val ticketSalesEntityService: TicketSalesEntityService
) {

    fun add(movieDtoList: List<MovieDto>): List<MovieEntity> {
        val movieEntityList = movieDtoList.map { movie -> movieMapper.toEntity(movie) }
        return movieEntityService.add(movieEntityList)
    }

    fun addToTicketSales(ticketSalesDto: TicketSalesDto): TicketSalesEntity {
        val ticketSalesEntity = movieMapper.toEntity(ticketSalesDto)
        return ticketSalesEntityService.add(ticketSalesEntity)
    }

    fun addToSeatsHistory(seatsDto: SeatsDto): SeatsEntity {
        val seatsEntity = movieMapper.toEntity(seatsDto)
        return seatsEntityService.add(seatsEntity)
    }

    fun findByBetweenDate(date: LocalDate): List<MovieEntity>? {
        return movieEntityService.findByBetweenDate(date)
    }

    fun generateDailyShowtimes(
        movieShowtimesDtoList: List<MovieShowtimesDto>,
        today: LocalDate
    ): List<MovieShowtimesEntity> {

        val newDtoList = movieShowtimesDtoList.flatMap { movie ->
            generateMovieShowtimesDtoInDetail(movie, 3, today)
        }

        val entityList = newDtoList.map { dto ->
            movieMapper.toEntity(dto)
        }

        val filteredEntityList = filterToExcludeAlreadyIn(entityList, today)

        return movieShowtimesEntityService.addDailyShowtimes(filteredEntityList)
    }

    fun getShowtimesWithSeats(date: LocalDate): List<MovieShowtimesWithSeatsDto> {
        val movieShowtimesEntityList = movieShowtimesEntityService.findByDate(date)

        return movieShowtimesEntityList.mapNotNull {
            val availableSeats = mapOnlyWithAvailableSeats(it)
            MovieShowtimesWithSeatsDto(it, availableSeats)
        }
    }


    fun getMovieByDateAndTitleAndStartTime(movieShowtimesDto: MovieShowtimesDto): MovieShowtimesEntity? {
        return movieShowtimesEntityService.findByDateAndTitleAndStartTime(movieMapper.toEntity(movieShowtimesDto))
    }

    fun isAlreadyTakenSeat(showtimesId: Long, seatNumber: String): Boolean {
        val a = seatsEntityService.findByShowtimesIdAndSeatNumber(showtimesId, seatNumber)
        return seatsEntityService.findByShowtimesIdAndSeatNumber(showtimesId, seatNumber) != null
    }

    private fun mapOnlyWithAvailableSeats(movieShowtimesEntity: MovieShowtimesEntity): List<String?> {
        val totalAvailableSeats = ('A'..'E').flatMap { aisle ->
            (1..10).map { number ->
                "$aisle${number.toString().padStart(2, '0')}"
            }
        }.toMutableList()

        val notAvailableSeats = seatsEntityService.findSeatsByShowtimesId(movieShowtimesEntity.id!!)
            .filter { it in totalAvailableSeats }

        totalAvailableSeats.removeAll(notAvailableSeats)

        return totalAvailableSeats
    }

    private fun generateMovieShowtimesDtoInDetail(movie: MovieShowtimesDto, numTimes: Int, today: LocalDate): MutableList<MovieShowtimesDto> {
        val newMovieShowtimesDtoList = mutableListOf<MovieShowtimesDto>()
        val startTime = LocalTime.of(9, 0)

        for (i in 0 until numTimes) {
            val startTimeWithInterval = startTime.plusMinutes(i * (movie.runningTime!!.toLong() + 100))
            val endTimeWithInterval = startTimeWithInterval.plusMinutes(movie.runningTime!!.toLong())

            newMovieShowtimesDtoList.add(
                MovieShowtimesDto(
                    date = today,
                    title = movie.title,
                    startTime = startTimeWithInterval,
                    endTime = endTimeWithInterval,
                    runningTime = movie.runningTime,
                    type = MovieShowtimesType.NORMAL,
                    price = movie.price,
                )
            )
        }
        return newMovieShowtimesDtoList
    }

    private fun filterToExcludeAlreadyIn(
        entityList: List<MovieShowtimesEntity>,
        today: LocalDate
    ): List<MovieShowtimesEntity> {
        val existingShowtimes = movieShowtimesEntityService.findByDate(today)

        val filteredEntityList = entityList.filterNot { newShowtime ->
            existingShowtimes.any { it ->
                newShowtime.startTime == it.startTime && newShowtime.title == it.title }
        }

        return filteredEntityList
    }

}

