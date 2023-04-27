package com.ivy.kiosk.controller.user.card

import com.ivy.kiosk.dto.user.card.CardDto
import com.ivy.kiosk.mapper.user.UserMapper
import com.ivy.kiosk.mapper.user.card.CardMapper
import com.ivy.kiosk.model.user.UserInfoModel
import com.ivy.kiosk.service.user.UserService
import com.ivy.kiosk.service.user.card.CardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user")
class CardController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val cardService: CardService,
    private val cardMapper: CardMapper,
) {

    @PostMapping("/card")
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
    @GetMapping("/card-number")
    fun getCardNumber(@RequestBody userInfoModel: UserInfoModel): String? {
        val userDto = userMapper.toDto(userInfoModel)
        val user = userService.getUserIdIfValidPassword(userDto)
        return user?.cardNumber
    }


}