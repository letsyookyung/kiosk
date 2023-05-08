package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import com.ivy.kiosk.dto.movie.BookTicketDto
import com.ivy.kiosk.dto.movie.SeatsDto
import com.ivy.kiosk.dto.movie.TicketSalesDto
import com.ivy.kiosk.mapper.movie.MovieMapper
import com.ivy.kiosk.service.user.card.CardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TicketSalesService(
    private val movieMapper: MovieMapper,
    private val cardService: CardService,
    private val seatsService: SeatsService,
    private val ticketSalesEntityService: TicketSalesEntityService,
) {
    fun addToTicketSales(ticketSalesDto: TicketSalesDto): TicketSalesDto {
        val ticketSalesEntity = movieMapper.toEntity(ticketSalesDto)
        return movieMapper.toDto(ticketSalesEntityService.add(ticketSalesEntity))
    }

    fun getTotalSalesByDate(date: LocalDate): Int? {
        val startDateTime = date.atStartOfDay()
        val endDateTime = startDateTime.plusDays(1).minusSeconds(1)
        return ticketSalesEntityService.getTotalSalesByDate(startDateTime, endDateTime)
    }

    @Transactional
    fun bookTicket(bookTicketDto: BookTicketDto): TicketSalesDto {
        cardService.updateBalance(bookTicketDto.cardNumber, -bookTicketDto.price!!)

        seatsService.addToSeatsHistory(SeatsDto(bookTicketDto.showtimesId, bookTicketDto.seatNumber))

        return addToTicketSales(TicketSalesDto(cardNumber = bookTicketDto.cardNumber, showtimesId = bookTicketDto.showtimesId))
    }

}