package com.ivy.kiosk.service.user.card

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dao.user.card.CardEntity
import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.repository.user.card.CardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CardEntityService(private val cardRepository: CardRepository) {

    fun add(cardEntity: CardEntity): CardEntity {
        return cardRepository.save(cardEntity)
    }

    fun updateBalance(cardNumber: String, amount: Int): Int {
        return cardRepository.updateBalance(cardNumber, amount)
    }

    fun findByCardNumber(cardNumber: String): CardEntity {
        return cardRepository.findByCardNumber(cardNumber)
    }

}


