package com.ivy.kiosk.controller.manager

import com.ivy.kiosk.service.manager.ManagerService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate


@RestController
@RequestMapping("/v1/manager")
class ManagerController(
    private val managerService: ManagerService,
) {

    @GetMapping("/daily-sales/{date}")
    fun getDailySales(@DateTimeFormat(pattern = "yyyy-MM-dd")@PathVariable date: LocalDate): ResponseEntity<Map<String, Int?>>  {
        val results = managerService.getDailySales(date)
        return ResponseEntity.status(HttpStatus.OK).body(mapOf(
            "카드 충전 금액" to results[0],
            "티켓 판매 금액" to results[1],
            "총 매출 금액" to results[2]
        ))
    }

}



