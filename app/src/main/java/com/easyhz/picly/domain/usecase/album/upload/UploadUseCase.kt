package com.easyhz.picly.domain.usecase.album.upload

import android.util.Log
import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.util.getImageSizes
import com.easyhz.picly.util.getImageUri
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadUseCase
@Inject constructor(
    private val repository: AlbumRepository
) {
    suspend fun writeAlbums(
        ownerId: String,
        tags: List<String>,
        selectedImageList: List<GalleryImageItem>,
        expireTime: Timestamp
    ): AlbumResult<String> = withContext(Dispatchers.IO) {
        val album = createAlbum(ownerId, tags, selectedImageList, expireTime)
        try {
            val documentId = repository.writeAlbums(album).first().id
            if (documentId.isBlank()) return@withContext AlbumResult.Error("잠시 후 다시 시도해주세요.")
            val imageUrls = repository.writeAlbumImages(documentId, selectedImageList.getImageUri()).first()
            val result = repository.updateAlbums(documentId, album.copy(imageUrls = imageUrls.imageUrls, thumbnailUrl = imageUrls.thumbnailUrl)).first()
            return@withContext AlbumResult.Success(result)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error writing albums and images: ${e.message}")
            return@withContext AlbumResult.Error("알 수 없는 오류가 발생하였습니다.")
        }
    }

    private fun createAlbum(
        ownerId: String,
        tags: List<String>,
        selectedImageList: List<GalleryImageItem>,
        expireTime: Timestamp
    ): Album {
        return Album(
            creationTime = Timestamp.now(),
            expireTime = expireTime,
            imageCount = selectedImageList.size,
            imageSizes = selectedImageList.getImageSizes(),
            ownerId = ownerId,
            tags = tags,
            viewCount = 0,
        )
    }
}