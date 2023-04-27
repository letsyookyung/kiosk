package com.ivy.kiosk.controller.user

import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.model.user.UserInfoModel
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/new")
    fun signUpForNewUser(@RequestBody userInfoModel: UserInfoModel): ResponseEntity<String> {
        val userDto = userMapper.toDto(userInfoModel)

        userService.add(userDto)

        logger.info("New user {} added", userInfoModel.name)

        return ResponseEntity.status(HttpStatus.OK).body("success")
    }

}

