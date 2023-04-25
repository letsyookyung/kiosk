package com.ivy.kiosk.controller.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.mapper.movie.MovieMapper
import com.ivy.kiosk.model.movie.MovieRequestModel
import com.ivy.kiosk.service.movie.MovieService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@RestController
@RequestMapping("/v1/movie")
class MovieController(
    private val movieMapper: MovieMapper,
    private val movieService: MovieService
) {

    @PostMapping("/add")
    fun addMovies(@RequestBody movieList: List<MovieRequestModel> ): List<MovieEntity> {
        val movieDtoList = movieList.map { movie -> movieMapper.toDto(movie)}

        return movieService.add(movieDtoList)

    }

    @PostMapping("/create-daily-showtimes")
    fun createDailyShowtimes(@DateTimeFormat(pattern = "yyyy-MM-dd")@RequestParam date: LocalDate) {
        val runningMovieList = movieService.findByBetweenDate(date)

        val movieShowtimesDtoList = runningMovieList?.let { movieMapper.createShowtimesDto(it, date) }

        if (movieShowtimesDtoList != null) {
           movieService.createDailyShowtimes(movieShowtimesDtoList, date) }
        }

}


