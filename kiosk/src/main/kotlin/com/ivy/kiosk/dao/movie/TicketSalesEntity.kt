package com.ivy.kiosk.dao.movie

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "ticket_sales_history")
class TicketSalesEntity {

    @Column(nullable = false)
    var cardNumber: String? = null

    @Column(nullable = true)
    val date: LocalDateTime? = LocalDateTime.now()

    @Column(nullable = true)
    var showtimesId: Long? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
}