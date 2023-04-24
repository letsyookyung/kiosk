package com.ivy.kiosk.service.user.card


import com.ivy.kiosk.dao.user.card.CardEntity
import com.ivy.kiosk.dto.user.card.CardDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.service.user.UserEntityService
import com.ivy.kiosk.service.user.UserService
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.random.Random


@Service
class CardService(
    private val userEntityService: UserEntityService,
    private val cardEntityService: CardEntityService,
    private val cardMapper: CardMapper,

) {

    fun addNewCard(cardDto: CardDto): CardDto {
        val cardEntity = cardMapper.toEntity(cardDto)
        return cardMapper.toDto(cardEntityService.add(cardEntity))
    }


    fun createUniqueCardNumber(): String {
        val cardNumber = generateRandomNumber()
        if (userEntityService.existsByCardNumber(cardNumber)) {
            throw IllegalArgumentException("중복되는 카드 번호가 있습니다")
        }
        return cardNumber
    }

    private fun generateRandomNumber(): String {
        val rand = Random.nextFloat()
        val floorRand = kotlin.math.floor(rand * 10.0.pow(16)).toLong()
        return String.format("%016d", floorRand)
    }


}



