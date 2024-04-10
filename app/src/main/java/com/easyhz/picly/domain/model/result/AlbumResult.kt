package com.easyhz.picly.domain.model.result

sealed class AlbumResult<T> {
    data class Success<T>(val value: T?): AlbumResult<T>()
    data class Error<T>(val errorMessage: String): AlbumResult<T>()
}