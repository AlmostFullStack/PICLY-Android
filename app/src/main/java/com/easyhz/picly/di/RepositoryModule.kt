package com.easyhz.picly.di

import com.easyhz.picly.data.repository.album.AlbumRepositoryImpl
import com.easyhz.picly.data.repository.user.UserRepositoryImpl
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}