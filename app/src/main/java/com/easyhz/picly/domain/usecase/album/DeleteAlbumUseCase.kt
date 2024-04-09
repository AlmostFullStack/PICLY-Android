package com.easyhz.picly.domain.usecase.album

import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.util.unknownErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteAlbumUseCase
@Inject constructor(
    private val repository: AlbumRepository
){
    sealed class DeleteAlbumResult {
        data object Success: DeleteAlbumResult()
        data class Error(val errorMessage: String): DeleteAlbumResult()
    }
    suspend operator fun invoke(id: String): DeleteAlbumResult = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.deleteAlbum(id).first()
            DeleteAlbumResult.Success
        } catch (e: Exception) {
            DeleteAlbumResult.Error(e.unknownErrorCode())
        }
    }
}