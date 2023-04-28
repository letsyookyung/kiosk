package com.ivy.kiosk.dao.movie

import javax.persistence.*

@Entity
@Table(name = "seats_history")
class SeatsEntity {

    @Column(nullable = false)
    var showtimesId: Long? = 0

    @Column(nullable = false)
    var seatNumber: String? = ""

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0

}