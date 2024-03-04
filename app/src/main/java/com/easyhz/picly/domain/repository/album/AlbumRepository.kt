package com.easyhz.picly.domain.repository.album

import android.net.Uri
import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.data.entity.album.ImageUrl
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun fetchAlbums() : Flow<List<Album>>

    fun writeAlbums(album: Album) : Flow<DocumentReference>

    fun updateAlbums(id: String, album: Album) : Flow<String>

    fun writeAlbumImages(id: String, imageUris: List<Uri>): Flow<ImageUrl>
}