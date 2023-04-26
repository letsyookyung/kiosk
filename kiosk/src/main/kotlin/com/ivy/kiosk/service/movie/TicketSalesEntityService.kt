package com.ivy.kiosk.service.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import com.ivy.kiosk.repository.movie.TicketSalesRepository
import org.springframework.stereotype.Service


@Service
class TicketSalesEntityService(
    private val ticketSalesRepository: TicketSalesRepository,
) {

    fun add(ticketSalesEntity: TicketSalesEntity): TicketSalesEntity {
        return ticketSalesRepository.save(ticketSalesEntity)
    }



}