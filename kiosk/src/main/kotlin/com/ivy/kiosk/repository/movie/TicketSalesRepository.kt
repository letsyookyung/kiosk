package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface TicketSalesRepository : JpaRepository<TicketSalesEntity, Long> {

    @Query("SELECT SUM(m.price) FROM MovieShowtimesEntity m JOIN TicketSalesEntity t ON m.id = t.showtimesId WHERE t.date BETWEEN :start AND :end")
    fun getTotalSalesByDate(start: LocalDateTime, end: LocalDateTime): Int?

}