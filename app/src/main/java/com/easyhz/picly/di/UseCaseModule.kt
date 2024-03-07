package com.easyhz.picly.di

import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.domain.repository.data_store.AppSettingRepository
import com.easyhz.picly.domain.repository.gallery.GalleryRepository
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.domain.usecase.album.AlbumUseCase
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import com.easyhz.picly.domain.usecase.album.upload.GalleryUseCase
import com.easyhz.picly.domain.usecase.album.upload.UploadUseCase
import com.easyhz.picly.domain.usecase.data_store.AppSettingDataStoreUseCase
import com.easyhz.picly.domain.usecase.settings.AccountUseCase
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
    fun provideDeleteAlbumUseCase(
        albumRepository: AlbumRepository
    ): DeleteAlbumUseCase = DeleteAlbumUseCase(albumRepository)

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

    @Provides
    @Singleton
    fun provideGalleryUseCase(
        galleryRepository: GalleryRepository
    ): GalleryUseCase = GalleryUseCase(galleryRepository)

    @Provides
    @Singleton
    fun provideUploadUseCase(
        albumRepository: AlbumRepository
    ): UploadUseCase = UploadUseCase(albumRepository)

    @Provides
    @Singleton
    fun provideAccountUseCase(
        userRepository: UserRepository
    ): AccountUseCase = AccountUseCase(userRepository)

    @Provides
    @Singleton
    fun provideAppSettingDataStoreUseCase(
        appSettingRepository: AppSettingRepository
    ): AppSettingDataStoreUseCase = AppSettingDataStoreUseCase(appSettingRepository)
}