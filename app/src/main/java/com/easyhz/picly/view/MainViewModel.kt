package com.easyhz.picly.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.domain.usecase.data_store.AppSettingDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val appSettingDataStoreUseCase: AppSettingDataStoreUseCase
): ViewModel() {
    private val _isFirstRun = MutableLiveData<Boolean>()
    val isFirstRun: MutableLiveData<Boolean> get() = _isFirstRun

    init {
        viewModelScope.launch {
            _isFirstRun.value = appSettingDataStoreUseCase.getIsFirstRun().first() ?: true
        }
    }

    fun updateFirstRunStatus() = viewModelScope.launch {
        appSettingDataStoreUseCase.setIsFirstRun()
    }

}