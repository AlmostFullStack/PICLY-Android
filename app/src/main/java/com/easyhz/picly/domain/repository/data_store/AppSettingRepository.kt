package com.easyhz.picly.domain.repository.data_store

import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {
    fun getFirstRun(): Flow<Boolean>
    suspend fun setFirstRunStatus()
}