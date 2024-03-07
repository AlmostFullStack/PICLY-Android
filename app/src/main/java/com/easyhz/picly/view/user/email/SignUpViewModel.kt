package com.easyhz.picly.view.user.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.usecase.user.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val signUpUseCase: SignUpUseCase
): ViewModel() {

    fun signUp(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) = viewModelScope.launch {
        isEmptyEmailAndPassword(email, password)?.let {
            onFailure(it)
            return@launch
        }
        signUpUseCase.signUp(
            UserForm(email, password),
            onSuccess = { onSuccess() }
        ) {
            if (it != null) {
                onFailure(it)
            }
        }
    }

    private fun isEmptyEmailAndPassword(email: String, password: String): String? = when {
        email.isEmpty() -> AuthError.ERROR_EMAIL_EMPTY.name
        password.isEmpty() -> AuthError.ERROR_PASSWORD_EMPTY.name
        else -> null
    }

}