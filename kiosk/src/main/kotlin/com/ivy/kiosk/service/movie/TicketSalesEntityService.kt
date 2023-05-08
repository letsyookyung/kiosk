package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import com.ivy.kiosk.repository.movie.TicketSalesRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime


@Service
class TicketSalesEntityService(
    private val ticketSalesRepository: TicketSalesRepository,
) {

    fun add(ticketSalesEntity: TicketSalesEntity): TicketSalesEntity {
        return ticketSalesRepository.save(ticketSalesEntity)
    }

    fun getTotalSalesByDate(start: LocalDateTime, end: LocalDateTime): Int? {
        return ticketSalesRepository.getTotalSalesByDate(start, end)
    }



}