package com.ivy.kiosk.controller.user

import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.model.user.UserInfoModel
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    companion object {
        val logger: Logger = LoggerFactory.getLogger(UserController::class.java)
    }

    @PostMapping("/new")
    fun signUpForNewUser(@RequestBody userInfoModel: UserInfoModel): ResponseEntity<String> {

        try {
            val userDto = userMapper.toDto(userInfoModel)
            userService.addUser(userDto)

            logger.info("New user {} added", userInfoModel.name)

            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            logger.error("{}: {}", userInfoModel.name, e.message, e)
            throw e
        }
    }


}

