package com.ivy.kiosk.controller

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Log4j2
@RestController
@RequestMapping("/v1/user")
class TestController {

    val logger: Logger = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping( "/log")
    fun log() : String {
        //FATAL, ERROR, WARN, INFO, DEBUG, TRACE
        logger.error("ERROR");
        logger.warn("WARN");
        logger.info("INFO");
        logger.debug("DEBUG");
        logger.trace("TRACE");
        return "success"
    }

}