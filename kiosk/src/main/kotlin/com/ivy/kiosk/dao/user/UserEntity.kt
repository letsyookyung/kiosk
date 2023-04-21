package com.ivy.kiosk.dao.user

import javax.persistence.Entity
import javax.persistence.*

@Table(name = "user")
@Entity
class UserEntity constructor (

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(nullable = false, length = 4)
    var password: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) {

}