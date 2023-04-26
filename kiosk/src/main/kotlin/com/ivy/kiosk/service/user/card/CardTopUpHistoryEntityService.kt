package com.ivy.kiosk.service.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.repository.user.card.CardTopUpHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime


@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CardTopUpHistoryEntityService(private val cardTopUpHistoryRepository: CardTopUpHistoryRepository) {

    fun add(cardTopUpHistoryEntity: CardTopUpHistoryEntity): CardTopUpHistoryEntity {
        return cardTopUpHistoryRepository.save(cardTopUpHistoryEntity)
    }

    fun getTotalTopUpAmountByDate(date: LocalDate): Int? {
        val startDateTime = date.atStartOfDay()
        val endDateTime = startDateTime.plusDays(1).minusSeconds(1)
        return cardTopUpHistoryRepository.getTotalTopUpAmountByDate(startDateTime, endDateTime)

    }


}