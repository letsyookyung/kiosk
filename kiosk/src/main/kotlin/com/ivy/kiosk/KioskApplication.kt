package com.ivy.kiosk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File


@SpringBootApplication
class KioskApplication

fun main(args: Array<String>) {
    runApplication<KioskApplication>(*args)

    val logsDirectory = File("./src/main/resources/logs")
    if (!logsDirectory.exists()) {
        logsDirectory.mkdir()
    }

}
