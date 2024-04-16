package com.easyhz.picly.domain.usecase.album

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.easyhz.picly.data.data_source.album.SearchAlbumPagingSource
import com.easyhz.picly.data.repository.album.AlbumRepositoryImpl.Companion.PAGE_SIZE
import com.easyhz.picly.domain.repository.album.AlbumRepository
import javax.inject.Inject

class SearchAlbumUseCase
@Inject constructor(
    private val repository: AlbumRepository
) {
    var searchText: String = ""
    private var pagingSource: SearchAlbumPagingSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                field = SearchAlbumPagingSource(repository, searchText)
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