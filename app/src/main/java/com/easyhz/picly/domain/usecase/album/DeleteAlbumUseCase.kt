package com.easyhz.picly.domain.usecase.album

import com.easyhz.picly.domain.model.result.AlbumResult
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
    suspend operator fun invoke(id: String): AlbumResult<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            repository.deleteAlbum(id).first()
            AlbumResult.Success(null)
        } catch (e: Exception) {
            AlbumResult.Error(e.unknownErrorCode())
        }
    }
}