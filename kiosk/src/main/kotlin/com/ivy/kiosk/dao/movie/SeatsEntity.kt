package com.ivy.kiosk.dao.movie

import javax.persistence.*

@Entity
@Table(name = "seats_history")
class SeatsEntity {

    @Column(nullable = true)
    var showtimesId: Long? = null

    @Column(nullable = true)
    var seatNumber: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}