package com.ivy.kiosk.service.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.dto.user.card.TopUpAmountDto
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import org.springframework.stereotype.Service

@Service
class CardTopUpHistoryService(
    private val cardTopUpHistoryMapper: CardTopUpHistoryMapper,
    private val cardTopUpHistoryEntityService: CardTopUpHistoryEntityService,
) {

    fun addCardTopUpHistory(topUpAmountDto: TopUpAmountDto): CardTopUpHistoryEntity? {
        val cardTopUpHistoryEntity = cardTopUpHistoryMapper.toEntity(topUpAmountDto)
        return cardTopUpHistoryEntityService.add(cardTopUpHistoryEntity)

    }
}