package com.easyhz.picly.view.settings.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.entity.user.UserInfo
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.usecase.settings.AccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject constructor(
    private val accountUseCase: AccountUseCase
): ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo : LiveData<UserInfo>
        get() = _userInfo

    fun fetchUserInfo() = viewModelScope.launch {
        UserManager.currentUser?.uid?.let { id ->
            accountUseCase.fetchLoginInfo(id).collectLatest { userInfo ->
                _userInfo.value = userInfo
            }
        }
    }

    fun logout(onSuccess: () -> Unit) = viewModelScope.launch {
        accountUseCase.logout (onSuccess)
    }

    fun deleteUser(onSuccess: () -> Unit) = viewModelScope.launch {
        accountUseCase.deleteUser(onSuccess)
    }
}