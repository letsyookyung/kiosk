package com.ivy.kiosk.service.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.mapper.user.UserMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userEntityService: UserEntityService,
    private val userMapper: UserMapper,
) {
    fun addUser(userDto: UserDto): UserEntity {
        return userEntityService.add(userMapper.toEntity(userDto)).also {
        }
    }

    fun isValidPassword(userId: Long, password: String) {
        val user = userEntityService.findById(userId)
            .orElseThrow { IllegalArgumentException("해당 카드번호를 가진 사용자가 존재하지 않습니다.") }
        require(user.password == password) { "입력하신 비밀번호가 틀렸습니다." }

    }

    fun getUserIdIfValidPassword(userDto: UserDto): UserEntity {
        val user = userEntityService.findByName(userMapper.toEntity(userDto))
            ?: throw IllegalArgumentException("입력하신 이름의 고객은 없습니다.")

        require(user.password == userDto.password) { "입력하신 비밀번호가 틀렸습니다." }

        return user
    }

    fun isAlreadyExistingUser(name: String) {
        require(!userEntityService.existsByName(name)) { "입력하신 이름은 이미 존재합니다, 다른 이름을 사용하세요."}
    }

}