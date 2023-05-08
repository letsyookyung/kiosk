package com.ivy.kiosk.controller.movie


import com.ivy.kiosk.dto.movie.*
import com.ivy.kiosk.exception.InsufficientBalanceException
import com.ivy.kiosk.mapper.movie.MovieMapper
import com.ivy.kiosk.model.movie.request.MovieBookingRequestModel
import com.ivy.kiosk.model.movie.request.MovieRequestModel
import com.ivy.kiosk.service.movie.MovieService
import com.ivy.kiosk.service.movie.MovieShowtimesService
import com.ivy.kiosk.service.movie.SeatsService
import com.ivy.kiosk.service.movie.TicketSalesService
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping("/v1/movie")
class MovieController(
    private val userService: UserService,
    private val cardService: CardService,
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val ticketSalesService: TicketSalesService,
    private val seatsService: SeatsService,
    private val movieShowtimesService: MovieShowtimesService,
) {
    val logger: Logger = LoggerFactory.getLogger(MovieController::class.java)

    @PostMapping("/new")
    fun addMovies(@RequestBody movieList: List<MovieRequestModel>): ResponseEntity<List<MovieDto>> {
        val movieDtoList = movieList.map { movie -> movieMapper.toDto(movie) }
        logger.info("Request received to add {} movies", movieList.size)
        val result = movieService.addMovie(movieDtoList)
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PostMapping("/generate-daily-showtimes")
    fun generateDailyShowtimes(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @RequestParam date: LocalDate
    ): ResponseEntity<List<MovieShowtimesDto>> {
        val runningMovieList = movieService.findByBetweenDate(date) ?: emptyList()

        return if (runningMovieList.isEmpty()) {
            logger.info("No running movies on $date")
            ResponseEntity.ok(emptyList())
        } else {
            val movieShowtimesDtoList = movieMapper.toDto(runningMovieList, date)
            val generatedMovieShowtimesDtoList = movieShowtimesService.generateDailyShowtimes(movieShowtimesDtoList, date)
            ResponseEntity.ok(generatedMovieShowtimesDtoList)
        }
    }

    @GetMapping("/daily-showtimes/{date}")
    fun getShowtimesWithSeats(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @PathVariable date: LocalDate
    ): ResponseEntity<List<MovieShowtimesWithSeatsDto>> { // 정리해서 보내기
        val showtimesWithSeats = movieShowtimesService.getShowtimesWithSeats(date)
        return if (showtimesWithSeats.isEmpty()) {
            ResponseEntity.ok(emptyList())
        } else {
            ResponseEntity.ok(showtimesWithSeats)
        }
    }

    @PostMapping("/ticket")
    fun bookTicket(@RequestBody @Valid movieBookingRequestModel: MovieBookingRequestModel): ResponseEntity<String> {
        try {
            val cardDto = cardService.findByCardNumber(movieBookingRequestModel.cardNumber)

            userService.isValidPassword(cardDto.userId, movieBookingRequestModel.password)

            val movieFromShowtimes = movieShowtimesService.getMovieByDateAndTitleAndStartTime(movieMapper.toDto(movieBookingRequestModel))
                ?: throw IllegalArgumentException("입력하신 영화가 존재하지 않습니다.")

            cardDto.balance.let {
                if (it != null) {
                    if (it <= movieFromShowtimes.price!!) {
                        throw InsufficientBalanceException("카드 잔액이 부족합니다. 충전 후 이용하십시오.")
                    }
                }
            }

            ticketSalesService.bookTicket(
                BookTicketDto(
                    movieBookingRequestModel.cardNumber,
                    movieFromShowtimes.id!!,
                    movieFromShowtimes.price!!,
                    movieBookingRequestModel.seatNumber
                )
            )

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


