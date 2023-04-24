package com.ivy.kiosk.repository.user

import com.ivy.kiosk.dao.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByName(name: String): UserEntity?

    fun existsByCardNumber(cardNumber: String): Boolean

    fun findByCardNumber(cardNumber: String): UserEntity?
}
