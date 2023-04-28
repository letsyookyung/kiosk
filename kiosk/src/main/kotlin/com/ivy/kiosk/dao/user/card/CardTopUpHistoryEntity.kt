package com.ivy.kiosk.dao.user.card

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "card_top_up_history")
class CardTopUpHistoryEntity {
    @Column(nullable = false, length = 16)
    var cardNumber: String = ""

    @Column(nullable = true)
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @Column(nullable = true)
    var amount: Int? = 0

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
}