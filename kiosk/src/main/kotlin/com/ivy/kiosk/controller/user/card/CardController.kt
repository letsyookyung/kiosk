package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.controller.movie.MovieController
import com.ivy.kiosk.dto.user.card.CardDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import com.ivy.kiosk.model.user.card.TopUpAmountModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import com.ivy.kiosk.service.user.card.CardTopUpHistoryService
import com.ivy.kiosk.model.user.UserInfoModel
import lombok.extern.slf4j.Slf4j
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
    private val cardMapper: CardMapper,
    private val cardTopUpHistoryMapper: CardTopUpHistoryMapper,
    private val cardTopUpHistoryService: CardTopUpHistoryService,
) {
    val logger: Logger = LoggerFactory.getLogger(CardController::class.java)


    @PostMapping("/card/new")
    fun issueNewCard(@RequestBody userInfoModel: UserInfoModel): ResponseEntity<String> {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)

        val cardNumber = cardService.createUniqueCardNumber()

        userService.updateCardNumber(user?.id!!, cardNumber)

        cardService.addNewCard(CardDto(null, user?.id!!, cardNumber, LocalDateTime.now(), 0))

        val message = "Card issued to user ${userInfoModel.name}. New card number: $cardNumber"
        logger.info(message)

        return ResponseEntity.ok().body(cardNumber).also { logger.info("Response sent: $it") }
    }

    @PostMapping("/card/money")
    fun topUpCardBalance(@RequestBody topUpAmountModel: TopUpAmountModel): ResponseEntity<String> {
        try {
            if (!userService.isValidCardToUse(topUpAmountModel.cardNumber, topUpAmountModel.password)) {
                throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
            }

            if (topUpAmountModel.amount % 50000 != 0 || topUpAmountModel.amount < 50000) {
                throw IllegalArgumentException("5만원 단위로 충전이 가능합니다.")
            }

            val topUpAmountDto = cardTopUpHistoryMapper.toDto(topUpAmountModel)

            cardTopUpHistoryService.addCardTopUpHistory(topUpAmountDto)

            cardService.updateBalance(topUpAmountModel.cardNumber, topUpAmountModel.amount)

            logger.info("Card {} was topped up by {} won", topUpAmountModel.cardNumber, topUpAmountModel.amount)

            return ResponseEntity.ok("success").also { logger.info("Response sent: $it") }
        } catch (e: Exception) {
            logger.error("{}: {}", topUpAmountModel.cardNumber, e.message)
            throw e
        }
    }

    @GetMapping("/card/balance")
    fun getMyBalance(@RequestParam cardNumber: String, @RequestParam password: String): ResponseEntity<Int> {
        try {
            if (!userService.isValidCardToUse(cardNumber, password)) {
                throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
            }

            val balance = cardService.getMyBalance(cardNumber)?.balance
                ?: throw IllegalArgumentException("카드 정보를 찾을 수 없습니다.")

            logger.info("Card {} balance is {}", cardNumber, balance)

            return ResponseEntity.ok(balance).also { logger.info("Response sent: $it") }
        } catch (e: Exception) {
            logger.error("{}: {}", cardNumber, e.message, e)
            throw e
        }
    }

    @GetMapping("/card-number")
    fun findCardNumber(@RequestParam name: String, @RequestParam password: String): ResponseEntity<String> {
        try {
            val userDto = userMapper.toDto(name, password)
            val user = userService.getUserIdIfValidPassword(userDto)

            if (user?.cardNumber == null) {
                throw IllegalArgumentException("카드를 발급 받으신 후 이용하세요.")
            }

            return ResponseEntity.ok(user.cardNumber).also { logger.info("Response sent: $it") }
        } catch (e: Exception) {
            logger.error("{}: {}", name, e.message)
            throw e
        }
    }


}