package com.easyhz.picly.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    private const val USER_DATASTORE = "user_datastore"
    @Provides
    @Singleton
    @Named("appSettingDataStorePreferences")
    fun provideUserPreferencesDataStore(@ApplicationContext context: Context) : DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_DATASTORE) }
        )
}