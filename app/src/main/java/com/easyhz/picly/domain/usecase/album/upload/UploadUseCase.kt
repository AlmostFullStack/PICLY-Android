package com.easyhz.picly.domain.usecase.album.upload

import android.util.Log
import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.util.getImageSizes
import com.easyhz.picly.util.getImageUri
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadUseCase
@Inject constructor(
    private val repository: AlbumRepository
) {

    fun writeAlbums(
        ownerId: String,
        tags: List<String>,
        selectedImageList: List<GalleryImageItem>,
        expireTime: Timestamp
    ): Flow<String> = flow {
        val album = createAlbum(ownerId, tags, selectedImageList, expireTime)
        try {
            var documentId = ""
            repository.writeAlbums(album).collectLatest {
                documentId = it.id
            }
            if (documentId.isBlank()) throw Exception()
            val imageUrls = repository.writeAlbumImages(documentId, selectedImageList.getImageUri()).first()
            updateAlbum(documentId, album.copy(imageUrls = imageUrls.imageUrls, thumbnailUrl = imageUrls.thumbnailUrl))

        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error writing albums and images: ${e.message}")
            throw e
        }
    }

    private suspend fun FlowCollector<String>.updateAlbum(documentId: String, album: Album) {
        val result = repository.updateAlbums(documentId, album).first()
        emit(result)
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