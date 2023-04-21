package com.ivy.kiosk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class KioskApplication

fun main(args: Array<String>) {
    runApplication<KioskApplication>(*args)

}
