package com.easyhz.picly.view.user.email

import androidx.lifecycle.ViewModel
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.data.firebase.Constants.AUTH_PROVIDER_EMAIL
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel
@Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    suspend fun login(email: String, password: String): LoginUseCase.LoginResult {
        isEmptyEmailAndPassword(email, password)?.let {
            return LoginUseCase.LoginResult.Error(it)
        }
        return loginUseCase.login(UserForm(email, password, AUTH_PROVIDER_EMAIL))
    }

    private fun isEmptyEmailAndPassword(email: String, password: String): String? = when {
        email.isEmpty() -> AuthError.ERROR_EMAIL_EMPTY.name
        password.isEmpty() -> AuthError.ERROR_PASSWORD_EMPTY.name
        else -> null
    }

}