package com.easyhz.picly.domain.repository.user

import com.easyhz.picly.data.entity.user.UserInfo
import com.easyhz.picly.domain.model.user.UserForm
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): FirebaseUser?

    suspend fun login(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit)

    fun fetchLoginInfo(id: String) : Flow<UserInfo>
    fun logout(onSuccess: () -> Unit)

    suspend fun signUp(user: UserForm)

    suspend fun deleteUser(id: String, onSuccess: () -> Unit)
}