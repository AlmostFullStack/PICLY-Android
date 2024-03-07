package com.easyhz.picly.view.user.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    private val _onErrorEvent = MutableLiveData<String>()
    val onErrorEvent: LiveData<String>
        get() = _onErrorEvent

    fun signUp(email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        if (isEmptyEmailAndPassword(email, password)) return@launch
        signUpUseCase.signUp(
            UserForm(email, password),
            onSuccess = { onSuccess() }
        ) {
            if (it != null) {
                onError(it)
            }
        }
    }

    private fun onError(errorId: String) {
        _onErrorEvent.postValue(errorId)
    }

    private fun isEmptyEmailAndPassword(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                onError(AuthError.ERROR_EMAIL_EMPTY.name)
                true
            }
            password.isEmpty() -> {
                onError(AuthError.ERROR_PASSWORD_EMPTY.name)
                true
            }
            else -> false
        }
    }

    fun setOnErrorEvent() {
        _onErrorEvent.postValue("")
    }

}