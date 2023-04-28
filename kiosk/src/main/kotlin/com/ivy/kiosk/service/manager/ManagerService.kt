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
        val cardTopUpAmount = cardTopUpHistoryService.getTotalTopUpAmountByDate(date) ?: 0
        val ticketSalesAmount = ticketSalesEntityService.getTotalSalesByDate(date) ?: 0
        val totalAmount = (cardTopUpAmount) + (ticketSalesAmount)

        return listOf(cardTopUpAmount, ticketSalesAmount, totalAmount)
    }




}