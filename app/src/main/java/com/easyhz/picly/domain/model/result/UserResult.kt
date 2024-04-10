package com.easyhz.picly.domain.model.result
sealed class UserResult {
    data object Success: UserResult()
    data class Error(val errorMessage: String): UserResult()
}