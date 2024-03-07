package com.easyhz.picly.domain.usecase.data_store

import com.easyhz.picly.domain.repository.data_store.AppSettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingDataStoreUseCase
@Inject constructor(
    private val repository: AppSettingRepository
) {

    fun getIsFirstRun(): Flow<Boolean> = repository.getFirstRun()

    suspend fun setIsFirstRun() {
        repository.setFirstRunStatus()
    }
}