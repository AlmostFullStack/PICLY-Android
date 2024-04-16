package com.easyhz.picly.domain.usecase.album

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.easyhz.picly.data.data_source.album.AlbumPagingSource
import com.easyhz.picly.data.repository.album.AlbumRepositoryImpl.Companion.PAGE_SIZE
import com.easyhz.picly.domain.repository.album.AlbumRepository
import javax.inject.Inject

class FetchAlbumUseCase
@Inject constructor(
    private val repository: AlbumRepository
) {
    private var pagingSource: AlbumPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = AlbumPagingSource(repository)
            }
            return field
        }
    operator fun invoke() = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE.toInt()
        ),
        pagingSourceFactory = {
           pagingSource!!
        }
    )

    fun setPagingSource() {
        pagingSource?.invalidate()
    }
}