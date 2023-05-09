package com.ivy.kiosk.controller.manager


import com.ivy.kiosk.service.manager.ManagerService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Slf4j
@RestController
@RequestMapping("/v1/manager")
class ManagerController(
    private val managerService: ManagerService,
) {
    val logger: Logger = LoggerFactory.getLogger(ManagerController::class.java)

    @GetMapping("/daily-sales/{date}")
    fun getDailySales(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable date: LocalDate): ResponseEntity<Map<String, Int?>> {
        try {
            val results = managerService.getDailySales(date)

            logger.info("Daily sales for {} retrieved: card top-up amount = {}, ticket sales amount = {}, total sales amount = {}",
                date, results[0], results[1], results[2])

            val responseBody = mapOf(
                "카드 충전 금액" to results[0],
                "티켓 판매 금액" to results[1],
                "총 매출 금액" to results[2]
            )

            return ResponseEntity.ok(responseBody)
        } catch (e: Exception) {
            logger.error("{}: {}", date, e.message, e)
            throw e
        }
    }


}



