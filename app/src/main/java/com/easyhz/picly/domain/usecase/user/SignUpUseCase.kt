package com.easyhz.picly.domain.usecase.user

import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import javax.inject.Inject

class SignUpUseCase
@Inject constructor(
    private val repository: UserRepository
){
    suspend fun signUp(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit) = repository.signUp(user, onSuccess, onError)
}