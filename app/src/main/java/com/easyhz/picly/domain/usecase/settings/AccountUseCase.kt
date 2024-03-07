package com.easyhz.picly.domain.usecase.settings

import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.repository.user.UserRepository
import javax.inject.Inject

class AccountUseCase
@Inject constructor(
    private val repository: UserRepository
){
    fun fetchLoginInfo(id: String) = repository.fetchLoginInfo(id)

    fun logout(onSuccess: () -> Unit) = repository.logout(onSuccess)

    suspend fun deleteUser(onSuccess: () -> Unit) =
        UserManager.currentUser?.uid?.let {
            repository.deleteUser(it, onSuccess)
        } ?: run { onSuccess }

}