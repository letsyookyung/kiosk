package com.ivy.kiosk.dao.user.card

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "cards")
class CardEntity {
    @Column(nullable = false)
    var userId: Long? = 0

    @Column(nullable = false, length = 16)
    var cardNumber: String? = ""

    @Column(nullable = true)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @Column(nullable = true)
    var balance: Int? = 0

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}