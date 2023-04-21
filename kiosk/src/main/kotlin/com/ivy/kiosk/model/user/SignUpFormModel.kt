package com.ivy.kiosk.model.user

import javax.validation.constraints.Size

data class SignUpFormModel(
    val name: String,
    val password: String,
)
