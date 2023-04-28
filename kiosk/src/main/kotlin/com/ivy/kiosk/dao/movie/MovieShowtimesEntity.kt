package com.ivy.kiosk.dao.movie

import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "showtimes")
class MovieShowtimesEntity {
    @Column(nullable = false)
    var date: LocalDate = LocalDate.now()

    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = true)
    var startTime: LocalTime? = null

    @Column(nullable = true)
    var endTime: LocalTime? = null

    @Column(nullable = true)
    var runningTime: Long? = null

    @Column(nullable = true)
    var type: MovieShowtimesType? = null

    @Column(nullable = true)
    var price: Int? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0

}