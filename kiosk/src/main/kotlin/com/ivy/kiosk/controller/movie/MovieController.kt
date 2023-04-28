package com.ivy.kiosk.controller.movie

import com.ivy.kiosk.controller.manager.ManagerController
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
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
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
    val logger: Logger = LoggerFactory.getLogger(MovieController::class.java)

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
        val runningMovieList = movieService.findByBetweenDate(date) ?: emptyList()

        return if (runningMovieList.isEmpty()) {
            logger.info("No running movies on $date")
            ResponseEntity.ok(emptyList())
        } else {
            val movieShowtimesDtoList = movieMapper.createShowtimesDto(runningMovieList, date)
            val generatedMovieShowtimesEntityList = movieService.generateDailyShowtimes(movieShowtimesDtoList, date)
            ResponseEntity.ok(generatedMovieShowtimesEntityList)
        }
    }

    @GetMapping("/daily-showtimes/{date}")
    fun getShowtimesWithSeats(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PathVariable date: LocalDate
    ): ResponseEntity<List<MovieShowtimesWithSeatsDto>> {
        val showtimesWithSeats = movieService.getShowtimesWithSeats(date)
        return if (showtimesWithSeats.isEmpty()) {
            ResponseEntity.ok(emptyList())
        } else {
            ResponseEntity.ok(showtimesWithSeats)
        }
    }

    @Transactional
    @PostMapping("/ticket")
    fun bookTicket(@RequestBody @Valid movieBookingRequestModel: MovieBookingRequestModel): ResponseEntity<String> {
        try {
            if (!userService.isCardValidToUse(movieBookingRequestModel.cardNumber, movieBookingRequestModel.password)) {
                logger.error("비밀번호가 일치하지 않습니다.")
                throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
            }

            val balance = cardService.getMyBalance(movieBookingRequestModel.cardNumber)?.balance
                ?: throw IllegalArgumentException("카드 정보를 찾을 수 없습니다.")

            val movieFromShowtimes = movieService.getMovieByDateAndTitleAndStartTime(movieMapper.toDto(movieBookingRequestModel))
                ?: throw IllegalArgumentException("입력하신 영화가 존재하지 않습니다.")

            balance.let {
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

            val result = """
             ${movieBookingRequestModel.date} ${movieBookingRequestModel.startTime} ${movieBookingRequestModel.title} 
             ${movieBookingRequestModel.seatNumber} 예약 완료 되었습니다.""".trimIndent()

            logger.info("Request received to book a ticket for ${movieBookingRequestModel.title} at ${movieBookingRequestModel.startTime} on ${movieBookingRequestModel.date}")

            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            logger.error("{}: {}", movieBookingRequestModel, e.message)
            throw e
        }
    }


}


