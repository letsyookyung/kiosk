package com.ivy.kiosk.service.manager

import com.ivy.kiosk.service.movie.TicketSalesEntityService
import com.ivy.kiosk.service.user.card.CardTopUpHistoryService
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class ManagerService(
    private val cardTopUpHistoryService: CardTopUpHistoryService,
    private val ticketSalesEntityService: TicketSalesEntityService,
    ) {

    fun getDailySales(date: LocalDate): List<Int?> {
        val cardTopUpAmount = cardTopUpHistoryService.getTotalTopUpAmountByDate(date)
        val ticketSalesAmount = ticketSalesEntityService.getTotalSalesByDate(date)
        val totalAmount = (cardTopUpAmount ?: 0) + (ticketSalesAmount ?: 0)

        return listOf(cardTopUpAmount, ticketSalesAmount, totalAmount)
    }




}