package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import com.ivy.kiosk.repository.movie.TicketSalesRepository
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class TicketSalesEntityService(
    private val ticketSalesRepository: TicketSalesRepository,
) {

    fun add(ticketSalesEntity: TicketSalesEntity): TicketSalesEntity {
        return ticketSalesRepository.save(ticketSalesEntity)
    }

    fun getTotalSalesByDate(date: LocalDate): Int? {
        val startDateTime = date.atStartOfDay()
        val endDateTime = startDateTime.plusDays(1).minusSeconds(1)
        return ticketSalesRepository.getTotalSalesByDate(startDateTime, endDateTime)
    }



}