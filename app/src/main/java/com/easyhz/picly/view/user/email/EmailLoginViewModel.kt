package com.easyhz.picly.view.user.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel
@Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        isEmptyEmailAndPassword(email, password)?.let {
            onError(it)
            return@launch
        }
        loginUseCase.login(
            UserForm(email, password),
            onSuccess = { onSuccess() }
        ) {
            if (it != null) {
                onError(it)
            }
        }
    }

    private fun isEmptyEmailAndPassword(email: String, password: String): String? = when {
        email.isEmpty() -> AuthError.ERROR_EMAIL_EMPTY.name
        password.isEmpty() -> AuthError.ERROR_PASSWORD_EMPTY.name
        else -> null
    }

}