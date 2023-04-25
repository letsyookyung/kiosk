package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesType
import com.ivy.kiosk.dto.movie.MovieDto
import com.ivy.kiosk.dto.movie.MovieShowtimesDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Service
class MovieService(
    private val movieMapper: MovieMapper,
    private val movieEntityService: MovieEntityService,
    private val movieShowtimesEntityService: MovieShowtimesEntityService,
) {

    fun add(movieDtoList: List<MovieDto>): List<MovieEntity> {
        val movieEntityList = movieDtoList.map {movie -> movieMapper.toEntity(movie) }
        return movieEntityService.add(movieEntityList)
    }

    fun findByBetweenDate(date: LocalDate): List<MovieEntity>? {
        return movieEntityService.findByBetweenDate(date)
    }

    fun createDailyShowtimes(movieShowtimesDtoList: List<MovieShowtimesDto>, today: LocalDate): List<MovieShowtimesEntity> {

        val newDtoList = movieShowtimesDtoList.flatMap { movie ->
            generateMovieShowtimesDtoInDetail(movie, 3, today) }

        val entityList = newDtoList.map { dto ->
            movieMapper.toEntity(dto) }
        val filteredEntityList = filterToExcludeAlreadyIn(entityList, today)

        return movieShowtimesEntityService.addDailyShowtimes(filteredEntityList)
    }

    fun getShowtimes(date: LocalDate): List<MovieShowtimesEntity>? {
        return movieShowtimesEntityService.findByDate(date)
    }


    private fun generateMovieShowtimesDtoInDetail(
        movie: MovieShowtimesDto,
        numTimes: Int,
        today: LocalDate)
    : MutableList<MovieShowtimesDto> {

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


    private fun filterToExcludeAlreadyIn(entityList: List<MovieShowtimesEntity>, today: LocalDate): List<MovieShowtimesEntity> {
        val existingShowtimes = movieShowtimesEntityService.findByDate(today)

        val filteredEntityList = entityList.filterNot { newShowtime ->
            existingShowtimes.any { it ->
                newShowtime.startTime == it.startTime && newShowtime.title == it.title }
        }

        return filteredEntityList
    }


}