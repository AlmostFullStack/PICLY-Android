package com.easyhz.picly.data.repository.data_store

import com.easyhz.picly.data.data_store.AppSettingDataStore
import com.easyhz.picly.domain.repository.data_store.AppSettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingRepositoryImpl
@Inject constructor(
    private val appSettingDataStore: AppSettingDataStore
): AppSettingRepository {

    override fun getFirstRun(): Flow<Boolean> = appSettingDataStore.isFirstRun

    override suspend fun setFirstRunStatus() {
        appSettingDataStore.updateFirstRunStatus()
    }
}