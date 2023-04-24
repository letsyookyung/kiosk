package com.ivy.kiosk.mapper.user.card

import com.ivy.kiosk.dao.user.card.CardEntity
import com.ivy.kiosk.dto.user.card.CardDto
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class CardMapper {
    abstract fun toEntity(dto: CardDto): CardEntity
    abstract fun toDto(entity: CardEntity): CardDto
}