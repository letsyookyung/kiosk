package com.ivy.kiosk.service.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.dto.user.card.TopUpAmountDto
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CardTopUpHistoryService(
    private val cardService: CardService,
    private val cardTopUpHistoryMapper: CardTopUpHistoryMapper,
    private val cardTopUpHistoryEntityService: CardTopUpHistoryEntityService,
) {

    fun addCardTopUpHistory(topUpAmountDto: TopUpAmountDto): TopUpAmountDto {
        cardService.updateBalance(topUpAmountDto.cardNumber, topUpAmountDto.amount!!)
        val cardTopUpHistoryEntity = cardTopUpHistoryMapper.toEntity(topUpAmountDto)
        return cardTopUpHistoryMapper.toDto(cardTopUpHistoryEntityService.add(cardTopUpHistoryEntity))

    }

    fun getTotalTopUpAmountByDate(date: LocalDate): Int? {
        return cardTopUpHistoryEntityService.getTotalTopUpAmountByDate(date)
    }

}