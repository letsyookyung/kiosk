package com.ivy.kiosk.repository.movie

import com.ivy.kiosk.dao.movie.TicketSalesEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TicketSalesRepository : JpaRepository<TicketSalesEntity, Long> {
}