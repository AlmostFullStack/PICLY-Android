package com.easyhz.picly.di

import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.domain.usecase.album.AlbumUseCase
import com.easyhz.picly.domain.usecase.user.UserUseCase
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

    @Provides
    @Singleton
    fun provideUserUseCase(
        userRepository: UserRepository
    ): UserUseCase = UserUseCase(userRepository)

}