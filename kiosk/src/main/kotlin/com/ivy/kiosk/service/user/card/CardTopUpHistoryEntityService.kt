package com.ivy.kiosk.service.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.repository.user.card.CardTopUpHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CardTopUpHistoryEntityService(private val cardTopUpHistoryRepository: CardTopUpHistoryRepository) {

    fun add(cardTopUpHistoryEntity: CardTopUpHistoryEntity): CardTopUpHistoryEntity {
        return cardTopUpHistoryRepository.save(cardTopUpHistoryEntity)
    }


}