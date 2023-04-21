package com.ivy.kiosk.mapper.user

import com.ivy.kiosk.dao.user.UserEntity
import com.ivy.kiosk.dto.user.UserDto
import com.ivy.kiosk.model.user.SignUpFormModel
import org.mapstruct.*
import org.mapstruct.ReportingPolicy
import org.springframework.stereotype.Component

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
abstract class UserMapper {

    abstract fun toDto(model: SignUpFormModel): UserDto

    abstract fun toEntity(dto: UserDto): UserEntity
}