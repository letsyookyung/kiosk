package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.dto.user.card.CardDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.model.user.card.IssueCardModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class CardController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val cardService: CardService,
    private val cardMapper: CardMapper,
) {

    @PostMapping("/card")
    fun issueNewCard(@RequestBody issueCardModel: IssueCardModel): String? {
        val userDto = userMapper.toDto(issueCardModel)
        val userId = userService.getUserIdIfValidPassword(userDto)

        val cardNumber = cardService.createUniqueCardNumber()

        userId?.let { userService.updateCardNumber(it, cardNumber) }

        return cardService.addNewCard(createCardDto(cardNumber)).cardNumber

    }


    private fun createCardDto(cardNumber: String): CardDto {
        return CardDto(cardNumber = cardNumber)
    }


}