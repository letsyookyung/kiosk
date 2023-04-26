package com.ivy.kiosk.controller.manager

import com.ivy.kiosk.service.manager.ManagerService
import org.springframework.format.annotation.DateTimeFormat
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
    fun getDailySales(@DateTimeFormat(pattern = "yyyy-MM-dd")@PathVariable date: LocalDate): Int {
        return managerService.getDailySales(date)
    }


}



