package com.easyhz.picly.di

import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.domain.usecase.album.AlbumUseCase
import com.easyhz.picly.domain.usecase.user.LoginUseCase
import com.easyhz.picly.domain.usecase.user.SignUpUseCase
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
    fun provideLoginUseCase(
        userRepository: UserRepository
    ): LoginUseCase = LoginUseCase(userRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(
        userRepository: UserRepository
    ): SignUpUseCase = SignUpUseCase(userRepository)
}