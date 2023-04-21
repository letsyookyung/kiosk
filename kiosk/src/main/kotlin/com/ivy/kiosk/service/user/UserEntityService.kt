package com.ivy.kiosk.service.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class UserEntityService(private val userRepository: UserRepository) {
    fun add(userEntity: UserEntity): UserEntity {
        return userRepository.save(userEntity)
    }

}