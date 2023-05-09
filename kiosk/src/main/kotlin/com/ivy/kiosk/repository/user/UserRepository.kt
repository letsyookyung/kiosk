package com.ivy.kiosk.repository.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dao.user.card.CardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByName(name: String): UserEntity?

    fun existsByCardNumber(cardNumber: String): Boolean

    fun existsByName(name: String): Boolean

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.cardNumber = :cardNumber WHERE u.id = :id")
    fun updateCardNumber(id: Long, cardNumber: String)
}
