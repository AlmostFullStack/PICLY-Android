package com.easyhz.picly.domain.usecase.user

import android.util.Log
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.util.unknownErrorCode
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpUseCase
@Inject constructor(
    private val repository: UserRepository
){
    sealed class SignUpResult {
        data object Success: SignUpResult()
        data class Error(val errorMessage: String): SignUpResult()
    }
    suspend fun signUp(user: UserForm): SignUpResult = withContext(Dispatchers.IO) {
        try {
            val result = repository.signUp(user)
            return@withContext SignUpResult.Success
        } catch (e: FirebaseAuthException) {
            return@withContext SignUpResult.Error(e.errorCode)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "SingUpUseCase Sign Up Error: ${e.message}")
            return@withContext SignUpResult.Error(e.unknownErrorCode())
        }
    }
}