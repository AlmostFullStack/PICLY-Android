package com.easyhz.picly.domain.repository.user

import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    fun getCurrentUser(): FirebaseUser?

    suspend fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String?) -> Unit)

    fun logout()
}