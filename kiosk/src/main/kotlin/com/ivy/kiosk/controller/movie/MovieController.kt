package com.ivy.kiosk.controller.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dao.movie.MovieShowtimesEntity
import com.ivy.kiosk.dto.movie.MovieShowtimesWithSeatsDto
import com.ivy.kiosk.dto.movie.TicketSalesDto
import com.ivy.kiosk.exception.InsufficientBalanceException
import com.ivy.kiosk.mapper.movie.MovieMapper
import com.ivy.kiosk.model.movie.request.MovieBookingRequestModel
import com.ivy.kiosk.model.movie.request.MovieRequestModel
import com.ivy.kiosk.service.movie.MovieService
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.io.File
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/v1/movie")
class MovieController(
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val userService: UserService,
    private val cardService: CardService,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/new")
    fun addMovies(@RequestBody movieList: List<MovieRequestModel>): ResponseEntity<List<MovieEntity>> {
        val movieDtoList = movieList.map { movie -> movieMapper.toDto(movie) }
        logger.info("Request received to add {} movies", movieList.size)

        return ResponseEntity.status(HttpStatus.OK).body(movieService.add(movieDtoList))

    }

    @PostMapping("/daily-showtimes")
    fun generateDailyShowtimes(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @RequestParam date: LocalDate
    ): ResponseEntity<List<MovieShowtimesEntity>> {
        val runningMovieList = movieService.findByBetweenDate(date)

        val movieShowtimesDtoList = runningMovieList?.let { movieMapper.createShowtimesDto(it, date) }

        val generatedMovieShowtimesEntityList = movieShowtimesDtoList?.let {
            movieService.generateDailyShowtimes(it, date)
        } ?: emptyList()

        return ResponseEntity.status(HttpStatus.OK).body(generatedMovieShowtimesEntityList)

    }

    @GetMapping("/daily-showtimes/{date}")
    fun getShowtimesWithSeats(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PathVariable date: LocalDate
    ): ResponseEntity<List<MovieShowtimesWithSeatsDto>> {

        if (movieService.getShowtimesWithSeats(date).isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(emptyList())
        }

        return ResponseEntity.status(HttpStatus.OK).body(movieService.getShowtimesWithSeats(date))
    }

    @Transactional
    @PostMapping("/ticket")
    fun bookTicket(@RequestBody @Valid movieBookingRequestModel: MovieBookingRequestModel): ResponseEntity<String> {
        if (!userService.isValidCardToUse(movieBookingRequestModel.cardNumber, movieBookingRequestModel.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        val balance = cardService.getMyBalance(movieBookingRequestModel.cardNumber).balance
        val movieFromShowtimes =
            movieService.getMovieByDateAndTitleAndStartTime(movieMapper.toDto(movieBookingRequestModel))
                ?: throw IllegalArgumentException("입력하신 영화가 존재하지 않습니다.")

        balance?.let {
            if (it <= movieFromShowtimes.price!!) {
                throw InsufficientBalanceException("카드 잔액이 부족합니다. 충전 후 이용하십시오.")
            }
        }

        if (movieService.isAlreadyTakenSeat(movieFromShowtimes.id!!, movieBookingRequestModel.seatNumber)) {
            throw IllegalArgumentException("입력하신 좌석은 이미 예매되었습니다. 다른 좌석을 입력하십시오.")
        }

        cardService.updateBalance(movieBookingRequestModel.cardNumber, -movieFromShowtimes.price!!)

        movieService.addToTicketSales(
            TicketSalesDto(
                cardNumber = movieBookingRequestModel.cardNumber,
                showtimesId = movieFromShowtimes.id!!
            )
        )

        movieService.addToSeatsHistory(movieMapper.toDto(movieFromShowtimes.id!!, movieBookingRequestModel))

        val result = "감사합니다. \n " +
                "${movieBookingRequestModel.date} ${movieBookingRequestModel.startTime} " +
                "${movieBookingRequestModel.title} ${movieBookingRequestModel.seatNumber} " +
                "예약 완료 되었습니다."

        logger.info("Request received to book a ticket for {} at {} on {}",
            movieBookingRequestModel.title, movieBookingRequestModel.startTime, movieBookingRequestModel.date)

        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

}


