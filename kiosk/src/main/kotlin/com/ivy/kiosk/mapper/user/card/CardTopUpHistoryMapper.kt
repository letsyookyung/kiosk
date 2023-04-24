package com.ivy.kiosk.mapper.user.card

import com.ivy.kiosk.dao.user.card.CardTopUpHistoryEntity
import com.ivy.kiosk.dto.user.card.TopUpAmountDto
import com.ivy.kiosk.model.user.card.TopUpAmountModel
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class CardTopUpHistoryMapper {

    abstract fun toDto(model: TopUpAmountModel): TopUpAmountDto

    abstract fun toEntity(dto: TopUpAmountDto): CardTopUpHistoryEntity

}
