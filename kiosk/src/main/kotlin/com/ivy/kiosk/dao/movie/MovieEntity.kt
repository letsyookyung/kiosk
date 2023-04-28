package com.ivy.kiosk.dao.movie

import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name = "movies")
class MovieEntity {
    @Column(nullable = false)
    var title: String = ""

    @Column(nullable = true)
    var runningTime: Int? = null

    @Column(nullable = true)
    var releaseDate: LocalDate? = null

    @Column(nullable = true)
    var endDate: LocalDate? = null

    @Column(nullable = true)
    var price: Int? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0
}


