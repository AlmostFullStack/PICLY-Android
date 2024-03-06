package com.easyhz.picly.domain.usecase.album

import com.easyhz.picly.domain.repository.album.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAlbumUseCase
@Inject constructor(
    private val repository: AlbumRepository
){
    operator fun invoke(id: String) : Flow<String> = repository.deleteAlbum(id)
}