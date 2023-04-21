package com.ivy.kiosk.mapper.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.model.user.SignUpFormModel
import com.ivy.kiosk.model.user.card.IssueCardModel
import org.mapstruct.*
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class UserMapper {

    abstract fun toDto(model: SignUpFormModel): UserDto
    abstract fun toDto(model: IssueCardModel): UserDto

    abstract fun toDto(entity: UserEntity): UserDto

    abstract fun toEntity(dto: UserDto): UserEntity
}