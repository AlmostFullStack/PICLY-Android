package com.easyhz.picly.domain.usecase.user

import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import javax.inject.Inject

class LoginUseCase
@Inject constructor(
    private val repository: UserRepository
) {
    suspend fun login(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit) = repository.login(user, onSuccess, onError)

}