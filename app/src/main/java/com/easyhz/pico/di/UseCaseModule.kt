package com.easyhz.pico.di

import com.easyhz.pico.domain.repository.album.AlbumRepository
import com.easyhz.pico.domain.usecase.album.AlbumUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAlbumUseCase(
        albumRepository: AlbumRepository
    ): AlbumUseCase = AlbumUseCase(albumRepository)

}