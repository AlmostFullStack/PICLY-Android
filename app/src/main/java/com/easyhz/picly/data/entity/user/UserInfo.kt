package com.easyhz.picly.data.entity.user

import com.google.firebase.Timestamp

data class UserInfo(
    val authProvider: String = "Email",
    val creationTime: Timestamp = Timestamp.now(),
    val email: String = ""
)
