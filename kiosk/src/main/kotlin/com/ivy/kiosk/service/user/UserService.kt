package com.ivy.kiosk.service.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.mapper.user.UserMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userEntityService: UserEntityService,
    private val userMapper: UserMapper,
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun add(userDto: UserDto) {
        userEntityService.add(userMapper.toEntity(userDto)).also {
            logger.info("Add User: {}", it)
        }
    }

    fun getUserIdIfValidPassword(userDto: UserDto): UserEntity {
        val user = getUserEntity(userDto)
        require(user.password == userDto.password) { "입력하신 비밀번호가 틀렸습니다." }
        return user
    }

    fun updateCardNumber(userId: Long, cardNumber: String) {
        userEntityService.updateCardNumber(userId, cardNumber).also {
            logger.info("Updated card number of User (ID: {}): {}", userId, cardNumber)
        }
    }

    fun isCardValidToUse(cardNumber: String, password: String): Boolean {
        val user = userEntityService.findByCardNumber(cardNumber) ?: return false
        return user.password == password
    }

    private fun getUserEntity(userDto: UserDto): UserEntity {
        return userEntityService.findByName(userMapper.toEntity(userDto))
            ?: throw IllegalArgumentException("입력하신 이름의 고객은 없습니다.")
    }

}