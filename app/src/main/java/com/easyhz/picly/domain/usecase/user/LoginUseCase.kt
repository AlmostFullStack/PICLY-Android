package com.easyhz.picly.domain.usecase.user

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
    sealed class LoginResult {
        data object Success: LoginResult()
        data class Error(val errorMessage: String): LoginResult()
    }
    suspend fun login(user: UserForm): LoginResult = withContext(Dispatchers.IO) {
        try {
            val result = repository.login(user)
            return@withContext LoginResult.Success
        } catch (e: FirebaseAuthException) {
            return@withContext LoginResult.Error(e.errorCode)
        } catch (e: Exception) {
            return@withContext LoginResult.Error(e.unknownErrorCode())
        }
    }

}