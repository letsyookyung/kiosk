package com.ivy.kiosk.service.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class UserEntityService(private val userRepository: UserRepository) {

    fun add(userEntity: UserEntity): UserEntity {
        return userRepository.save(userEntity)
    }

    fun findByName(userEntity: UserEntity): UserEntity? {
        return userEntity.name?.let { userRepository.findByName(it) }
    }

    fun findById(id: Long): Optional<UserEntity> {
        return userRepository.findById(id)
    }

    fun existsByCardNumber(cardNumber: String): Boolean{
        return userRepository.existsByCardNumber(cardNumber)
    }


}