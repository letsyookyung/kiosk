package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import com.ivy.kiosk.model.user.card.TopUpAmountModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import com.ivy.kiosk.service.user.card.CardTopUpHistoryService
import com.ivy.kiosk.model.user.UserInfoModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/card/new")
    fun issueNewCard(@RequestBody userInfoModel: UserInfoModel): ResponseEntity<String> {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)

        val cardNumber = cardService.createUniqueCardNumber()

         val cardEntity = user?.let{
            userService.updateCardNumber(it.id!!, cardNumber)
            cardService.addNewCard(cardMapper.toDto(user.id!!, cardNumber))
        }
        return ResponseEntity.status(HttpStatus.OK).body(cardEntity?.cardNumber)
    }

    @PostMapping("/card/money")
    fun topUpCardBalance(@RequestBody topUpAmountModel: TopUpAmountModel): ResponseEntity<String> {
        if (!userService.isValidCardToUse(topUpAmountModel.cardNumber, topUpAmountModel.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        if (topUpAmountModel.amount % 50000 != 0 || topUpAmountModel.amount < 50000) {
            throw IllegalArgumentException("5만원 단위로 충전이 가능합니다.")
        }

        val topUpAmountDto = cardTopUpHistoryMapper.toDto(topUpAmountModel)

        cardTopUpHistoryService.addCardTopUpHistory(topUpAmountDto)

        return if (cardService.updateBalance(topUpAmountModel.cardNumber, topUpAmountModel.amount) >= 1) {
            ResponseEntity.status(HttpStatus.OK).body("success")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경된 것이 없음")
        }

    }

    @GetMapping("/card/balance/")
    fun getMyBalance(@RequestParam cardNumber: String, @RequestParam password: String): ResponseEntity<Int> {
        if (!userService.isValidCardToUse(cardNumber, password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        return ResponseEntity.status(HttpStatus.OK).body(cardService.getMyBalance(cardNumber).balance)

    }

    @GetMapping("/card-number")
    fun findCardNumber(@RequestParam name: String, @RequestParam password: String): ResponseEntity<String> {
        val userDto = userMapper.toDto(name, password)
        val user = userService.getUserIdIfValidPassword(userDto)

        return ResponseEntity.status(HttpStatus.OK).body(user?.cardNumber)
    }

}