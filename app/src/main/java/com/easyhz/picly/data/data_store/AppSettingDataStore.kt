package com.easyhz.picly.data.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class AppSettingDataStore
@Inject constructor(
    @Named("appSettingDataStorePreferences") private val dataStore: DataStore<Preferences>,
){

    val isFirstRun: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_RUN_KEY] ?: true
        }

    suspend fun updateFirstRunStatus() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN_KEY] = false
        }
    }

    companion object {
        private val IS_FIRST_RUN_KEY = booleanPreferencesKey("isFirstRun")
    }
}