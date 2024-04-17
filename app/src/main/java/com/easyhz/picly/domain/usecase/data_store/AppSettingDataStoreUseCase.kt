package com.easyhz.picly.domain.usecase.data_store

import com.easyhz.picly.domain.repository.data_store.AppSettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppSettingDataStoreUseCase
@Inject constructor(
    private val repository: AppSettingRepository
) {

    suspend fun getIsFirstRun(): Flow<Boolean> = withContext(Dispatchers.IO) {
        repository.getFirstRun()
    }

    suspend fun setIsFirstRun() = withContext(Dispatchers.IO) {
        repository.setFirstRunStatus()
    }
}