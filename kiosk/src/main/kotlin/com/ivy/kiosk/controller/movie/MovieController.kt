package com.ivy.kiosk.controller.movie

import com.ivy.kiosk.dao.movie.MovieEntity
import com.ivy.kiosk.dto.movie.MovieShowtimesWithSeatsDto
import com.ivy.kiosk.dto.movie.TicketSalesDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import com.ivy.kiosk.model.movie.request.MovieBookingRequestModel
import com.ivy.kiosk.model.movie.request.MovieRequestModel
import com.ivy.kiosk.service.movie.MovieService
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid
import kotlin.system.exitProcess

@RestController
@RequestMapping("/v1/movie")
class MovieController(
    private val movieMapper: MovieMapper,
    private val movieService: MovieService,
    private val userService: UserService,
    private val cardService: CardService,
) {

    @PostMapping("/add")
    fun addMovies(@RequestBody movieList: List<MovieRequestModel> ): List<MovieEntity> {
        val movieDtoList = movieList.map { movie -> movieMapper.toDto(movie)}

        return movieService.add(movieDtoList)

    }

    @PostMapping("/generateDailyShowtimes")
    fun generateDailyShowtimes(@DateTimeFormat(pattern = "yyyy-MM-dd")@RequestParam date: LocalDate) {
        val runningMovieList = movieService.findByBetweenDate(date)

        val movieShowtimesDtoList = runningMovieList?.let { movieMapper.createShowtimesDto(it, date) }

        if (movieShowtimesDtoList != null) {
           movieService.generateDailyShowtimes(movieShowtimesDtoList, date) }
         }

    @GetMapping("/showtimes/{date}")
    fun getShowtimesWithSeats(@DateTimeFormat(pattern = "yyyy-MM-dd")@PathVariable date: LocalDate): List<MovieShowtimesWithSeatsDto> {
        return movieService.getShowtimesWithSeats(date)

    }

    @Transactional
    @PostMapping("/ticket")
    fun bookTicket(@RequestBody @Valid movieBookingRequestModel: MovieBookingRequestModel) {
        if (!userService.isValidCardToUse(movieBookingRequestModel.cardNumber, movieBookingRequestModel.password)) {
            throw IllegalArgumentException("입력하신 비밀번호와 입력하신 카드번호의 비밀번호가 일치하지 않습니다.")
        }

        val balance = cardService.getMyBalance(movieBookingRequestModel.cardNumber).balance
        val movieFromShowtimes = movieService.getMovieByDateAndTitleAndStartTime(movieMapper.toDto(movieBookingRequestModel))
            ?: throw IllegalArgumentException("입력하신 영화가 존재하지 않습니다.")

        balance?.let {
            if (it <= movieFromShowtimes.price!!) {
                throw IllegalArgumentException("카드 잔액이 부족합니다. 충전하십시오.")
            }
        }

        // todo 좌석 이미 있는지 확인
        if (movieService.isAlreadyTakenSeat(movieFromShowtimes.id!!, movieBookingRequestModel.seatNumber)){
            throw IllegalArgumentException("입력하신 좌석은 이미 예매되었습니다. 다른 좌석을 입력하십시오.")
        }

        // todo 카드번호로 조회해서 card totalbalance 차감
        cardService.updateBalance(movieBookingRequestModel.cardNumber, -movieFromShowtimes.price!!)

        // todo 우선 예약해서 ticekt_sales_history 테이블에 쌓음
        movieService.addToTicketSales(TicketSalesDto(cardNumber = movieBookingRequestModel.cardNumber, showtimesId = movieFromShowtimes.id!!))

        // todo seats_history 에도 쌓음
        movieService.addToSeatsHistory(movieMapper.toDto(movieFromShowtimes.id!!, movieBookingRequestModel))


    }



}


