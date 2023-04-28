package com.ivy.kiosk.dao.user

import javax.persistence.Entity
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity {
    @Column(nullable = false, length = 50)
    var name: String? = null

    @Column(nullable = false, length = 4)
    var password: String? = null

    @Column(nullable = true, length = 16)
    var cardNumber: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
}