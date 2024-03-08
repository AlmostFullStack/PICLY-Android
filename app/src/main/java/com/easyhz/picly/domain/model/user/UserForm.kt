package com.easyhz.picly.domain.model.user

data class UserForm(
    val email: String,
    val password: String,
    val authProvider: String,
    val uid: String = ""
)
