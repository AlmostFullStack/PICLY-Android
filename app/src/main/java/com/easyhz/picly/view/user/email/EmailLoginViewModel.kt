package com.easyhz.picly.view.user.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    val _onSuccessEvent = MutableLiveData<Unit>()
    val onSuccessEvent: LiveData<Unit>
        get() = _onSuccessEvent

    val _onErrorEvent = MutableLiveData<String>()
    val onErrorEvent: LiveData<String>
        get() = _onErrorEvent

    fun login(email: String, password: String) = viewModelScope.launch {
        if (isEmptyEmailAndPassword(email, password)) return@launch
        loginUseCase.login(
            UserForm(email, password),
            onSuccess = { onSuccess() }
        ) {
            if (it != null) {
                onError(it)
            }
        }
    }

    fun setOnErrorEvent() {
        _onErrorEvent.postValue("")
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

    private fun onSuccess() {
        _onSuccessEvent.postValue(Unit)
    }

    private fun onError(errorId: String) {
        _onErrorEvent.postValue(errorId)
    }

}