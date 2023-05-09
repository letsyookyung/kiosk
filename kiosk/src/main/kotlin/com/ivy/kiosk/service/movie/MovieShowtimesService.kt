package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesType
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.dto.movie.MovieShowtimesWithSeatsDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class MovieShowtimesService(
    private val movieMapper: MovieMapper,
    private val movieShowtimesEntityService: MovieShowtimesEntityService,
    private val seatsEntityService: SeatsEntityService,
) {

    fun generateDailyShowtimes(
        movieShowtimesDtoList: List<MovieShowtimesDto>,
        today: LocalDate
    ): List<MovieShowtimesDto> {

        val newDtoList = movieShowtimesDtoList.flatMap { movie ->
            generateMovieShowtimesDtoInDetail(movie, 3, today)
        }

        val entityList = newDtoList.map { dto ->
            movieMapper.toEntity(dto)
        }

        val filteredEntityList = filterToExcludeAlreadyIn(entityList, today)

        return movieShowtimesEntityService.addDailyShowtimes(filteredEntityList).map { movieShowtimesEntity -> movieMapper.toDto(movieShowtimesEntity) }

    }


    fun getShowtimesWithSeats(date: LocalDate): List<MovieShowtimesWithSeatsDto> {
        val movieShowtimesEntityList = movieShowtimesEntityService.findByDate(date)

        if (movieShowtimesEntityList != null) {
            return movieShowtimesEntityList.mapNotNull {
                val availableSeats = mapOnlyWithAvailableSeats(it)
                MovieShowtimesWithSeatsDto(it, availableSeats)
            }
        }
        return emptyList()
    }


    fun getMovieByDateAndTitleAndStartTime(movieShowtimesDto: MovieShowtimesDto): MovieShowtimesEntity? {
        return movieShowtimesEntityService.findByDateAndTitleAndStartTime(movieMapper.toEntity(movieShowtimesDto))
    }


    private fun generateMovieShowtimesDtoInDetail(movie: MovieShowtimesDto, numTimes: Int, today: LocalDate): List<MovieShowtimesDto> {
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
        return entityList.filterNot { newShowtime ->
            existingShowtimes?.any { it.startTime == newShowtime.startTime && it.title == newShowtime.title } ?: false
        }
    }


    private fun mapOnlyWithAvailableSeats(movieShowtimesEntity: MovieShowtimesEntity): List<String?> {
        val totalSeats = ('A'..'E').flatMap { aisle ->
            (1..10).map { number ->
                "$aisle${number.toString().padStart(2, '0')}"
            }
        }.toMutableList()

        val occupiedSeats = seatsEntityService.findSeatsByShowtimesId(movieShowtimesEntity.id!!)

        return totalSeats - occupiedSeats.toSet()
    }

}