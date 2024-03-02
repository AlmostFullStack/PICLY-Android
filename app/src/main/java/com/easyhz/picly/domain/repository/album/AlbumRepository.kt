package com.easyhz.picly.domain.repository.album

import com.easyhz.picly.data.entity.album.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun fetchAlbums() : Flow<List<Album>>

//    fun writeAlbums() : Flow<>

}