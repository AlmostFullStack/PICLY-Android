package com.easyhz.picly.domain.usecase.user

import com.easyhz.picly.domain.model.result.UserResult
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.util.unknownErrorCode
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase
@Inject constructor(
    private val repository: UserRepository
) {
    suspend fun login(user: UserForm): UserResult = withContext(Dispatchers.IO) {
        try {
            val result = repository.login(user)
            return@withContext UserResult.Success
        } catch (e: FirebaseAuthException) {
            return@withContext UserResult.Error(e.errorCode)
        } catch (e: Exception) {
            return@withContext UserResult.Error(e.unknownErrorCode())
        }
    }

}