package com.ivy.kiosk.model.user

import javax.validation.constraints.Size

data class SignUpFormModel(
    val name: String,

    @field:Size(max = 4, min = 4)
    val password: Int,
)
