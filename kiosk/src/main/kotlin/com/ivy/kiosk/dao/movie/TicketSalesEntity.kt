package com.ivy.kiosk.dao.movie

import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "ticket_sales_history")
class TicketSalesEntity {

    @Column(nullable = true)
    var showtimesId: Long? = null

    @Column(nullable = true)
    var cardNumber: String? = null

    @Column(nullable = true)
    val date: LocalDateTime? = LocalDateTime.now()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}