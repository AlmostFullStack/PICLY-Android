package com.easyhz.picly.domain.usecase.user

import com.easyhz.picly.domain.repository.user.UserRepository
import javax.inject.Inject

class UserUseCase
@Inject constructor(
    private val repository: UserRepository
) {
    suspend fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String?) -> Unit) = repository.login(email, password, onSuccess, onError)

}