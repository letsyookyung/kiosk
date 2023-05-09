package com.ivy.kiosk.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.format.DateTimeParseException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class, InsufficientBalanceException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(ex.message)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class, DateTimeParseException::class)
    fun handleInvalidDateFormatException(ex: HttpMessageNotReadableException): ResponseEntity<Any> {
        val errorMessage = "입력 형식이 잘못되었습니다. 올바른 형식(예: 'yyyy-MM-dd', '09:00', '18:30')으로 날짜를 입력해주세요."
        return ResponseEntity.badRequest().body(errorMessage)
    }


}
class InsufficientBalanceException(message: String?) : RuntimeException(message)

