package com.ivy.kiosk.service.user

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
}