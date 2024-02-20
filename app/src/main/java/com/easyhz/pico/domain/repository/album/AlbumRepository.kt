package com.easyhz.pico.domain.repository.album

import com.easyhz.pico.data.entity.album.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun fetchAlbums() : Flow<List<Album>>

}