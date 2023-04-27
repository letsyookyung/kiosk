package com.ivy.kiosk.service.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.mapper.user.UserMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userEntityService: UserEntityService,
    private val userMapper: UserMapper,
) {

    fun add(userDto: UserDto) {
        val userEntity = userEntityService.add(userMapper.toEntity(userDto))
    }


    fun getUserIdIfValidPassword(userDto: UserDto): UserEntity? {
        val user = getUserInfo(userDto)
        if (!user!!.password.equals(userDto.password)) {
            throw IllegalArgumentException("입력하신 비밀번호가 틀렸습니다.")
        } else {
            return user
        }
    }

    fun updateCardNumber(userId: Long, cardNumber: String) {
        return userEntityService.updateCardNumber(userId, cardNumber)
    }


    fun isValidCardToUse(cardNumber: String, password: String): Boolean {
        val userEntity =
            userEntityService.findByCardNumber(cardNumber) ?: throw IllegalArgumentException("입력하신 카드번호가 존재하지 않습니다.")

        return userEntity?.let { it.password.equals(password) }
    }

    private fun getUserInfo(userDto: UserDto): UserEntity? {
        val userEntity = userMapper.toEntity(userDto)
        return userEntityService.findByName(userEntity) ?: throw IllegalArgumentException("입력하신 이름의 고객은 없습니다.")
    }

}