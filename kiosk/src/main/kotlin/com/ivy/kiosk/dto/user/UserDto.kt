package com.ivy.kiosk.dto.user

data class UserDto(
    val id: Long? = 0,
    val name: String,
    val password: String,
) {
}