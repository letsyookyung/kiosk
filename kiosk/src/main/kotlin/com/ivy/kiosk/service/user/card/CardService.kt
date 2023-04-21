package com.ivy.kiosk.service.user.card


import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.service.user.UserEntityService
import com.ivy.kiosk.service.user.UserService
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.random.Random


@Service
class CardService(
    private val userService: UserService,
    private val userEntityService: UserEntityService,
    private val userMapper: UserMapper
) {

    fun issueCardNumber(userId: Long?): UserDto? {
        val cardNumber = generateRandomNumber()

        if (userEntityService.existsByCardNumber(cardNumber)) {
            throw IllegalArgumentException("중복되는 카드 번호가 있습니다")
        }

        val userEntity = userId?.let { userEntityService.updateCardNumber(it, cardNumber) }

        return userEntity?.let { userMapper.toDto(it) }

    }


    private fun generateRandomNumber(): String {
        val rand = Random.nextFloat()
        val floorRand = kotlin.math.floor(rand * 10.0.pow(16)).toLong()
        return String.format("%016d", floorRand)
    }

}



