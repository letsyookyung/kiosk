package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.mapper.user.card.CardTopUpHistoryMapper
import com.ivy.kiosk.model.user.card.TopUpAmountModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import com.ivy.kiosk.service.user.card.CardTopUpHistoryService
import com.ivy.kiosk.model.user.UserInfoModel
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
    fun issueNewCard(@RequestBody userInfoModel: UserInfoModel): String? {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)

        val cardNumber = cardService.createUniqueCardNumber()

         val cardEntity = user?.let{
            userService.updateCardNumber(it.id!!, cardNumber)
            cardService.addNewCard(cardMapper.toDto(user.id!!, cardNumber))
        }
        return cardEntity?.cardNumber
    }

    @PostMapping("/card/money")
    fun topUpCardBalance(@RequestBody topUpAmountModel: TopUpAmountModel): Int {
        if (!userService.isValidCardToUse(topUpAmountModel.cardNumber, topUpAmountModel.password)) {
            throw IllegalArgumentException("입력하신 비밀번호와 입력하신 카드번호의 비밀번호가 일치하지 않습니다.")
        }

        if (topUpAmountModel.amount % 50000 != 0 || topUpAmountModel.amount < 50000) {
            throw IllegalArgumentException("5만원 단위로 충전이 가능합니다.")
        }

        val topUpAmountDto = cardTopUpHistoryMapper.toDto(topUpAmountModel)

        cardTopUpHistoryService.addCardTopUpHistory(topUpAmountDto)

        return cardService.updateBalance(topUpAmountModel.cardNumber, topUpAmountModel.amount)
    }

    @GetMapping("/card/balance/")
    fun getMyBalance(@RequestParam cardNumber: String, @RequestParam password: String): Int? {
        if (!userService.isValidCardToUse(cardNumber, password)) {
            throw IllegalArgumentException("입력하신 비밀번호와 입력하신 카드번호의 비밀번호가 일치하지 않습니다.")
        }

        return cardService.getMyBalance(cardNumber).balance

    }

    @GetMapping("/card-number")
    fun getCardNumber(@RequestBody userInfoModel: UserInfoModel): String? {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)
        return user?.cardNumber
    }

}