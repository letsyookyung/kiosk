package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.dto.user.card.CardDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import com.ivy.kiosk.model.user.card.TopUpAmountModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import com.ivy.kiosk.service.user.card.CardTopUpHistoryService
import com.ivy.kiosk.model.user.UserInfoModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/user")
class CardController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val cardService: CardService,
    private val cardTopUpHistoryMapper: CardTopUpHistoryMapper,
    private val cardTopUpHistoryService: CardTopUpHistoryService,
) {
    val logger: Logger = LoggerFactory.getLogger(CardController::class.java)

    @PostMapping("/card/new")
    fun issueNewCard(@RequestBody userInfoModel: UserInfoModel): ResponseEntity<String> {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)

        val cardNumber = cardService.createUniqueCardNumber()

        cardService.addCard(CardDto(null, user.id!!, cardNumber, LocalDateTime.now(), 0))

        val message = "Card issued to user ${userInfoModel.name}. New card number: $cardNumber"
        logger.info(message)

        return ResponseEntity.ok().body(cardNumber)
    }

    @PostMapping("/card/money")
    fun topUpCardBalance(@RequestBody topUpAmountModel: TopUpAmountModel): ResponseEntity<String> {
        try {
            val cardDto = cardService.findByCardNumber(topUpAmountModel.cardNumber)

            userService.isValidPassword(cardDto.userId, topUpAmountModel.password)

            if (topUpAmountModel.amount % 50000 != 0 || topUpAmountModel.amount < 50000) {
                throw IllegalArgumentException("5만원 단위로 충전이 가능합니다.")
            }

            val topUpAmountDto = cardTopUpHistoryMapper.toDto(topUpAmountModel)

            cardTopUpHistoryService.addCardTopUpHistory(topUpAmountDto)

            logger.info("Card {} was topped up by {} won", topUpAmountModel.cardNumber, topUpAmountModel.amount)

            return ResponseEntity.ok("success")
        } catch (e: Exception) {
            logger.error("{}: {}", topUpAmountModel.cardNumber, e.message)
            throw e
        }
    }

    @GetMapping("/card/balance")
    fun getMyBalance(@RequestParam cardNumber: String, @RequestParam password: String): ResponseEntity<Int> {
        try {
            val cardDto = cardService.findByCardNumber(cardNumber)

            userService.isValidPassword(cardDto.userId, password)

            logger.info("Card {} balance is {}", cardNumber, cardDto.balance)

            return ResponseEntity.ok(cardDto.balance)
        } catch (e: Exception) {
            logger.error("{}: {}", cardNumber, e.message, e)
            throw e
        }
    }

    @GetMapping("/card-number")
    fun findCardNumber(@RequestParam name: String, @RequestParam password: String): ResponseEntity<String> {
        try {
            val userDto = userMapper.toDto(name, password)
            val user = userMapper.toDto(userService.getUserIdIfValidPassword(userDto))

            cardService.findByUserId(user.id!!)

            return ResponseEntity.ok(cardService.findByUserId(user.id!!).cardNumber)
        } catch (e: Exception) {
            logger.error("{}: {}", name, e.message)
            throw e
        }
    }


}