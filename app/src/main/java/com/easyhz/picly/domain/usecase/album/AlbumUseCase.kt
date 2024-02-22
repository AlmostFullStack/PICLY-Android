package com.easyhz.picly.domain.usecase.album

import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.domain.repository.album.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumUseCase
@Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke() : Flow<List<Album>> = repository.fetchAlbums()

}