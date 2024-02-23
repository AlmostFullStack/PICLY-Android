package com.easyhz.picly.domain.repository.user

import com.easyhz.picly.domain.model.user.UserForm
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getCurrentUser(): FirebaseUser?

    suspend fun login(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit)

    fun logout()

    suspend fun signUp(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit)
}