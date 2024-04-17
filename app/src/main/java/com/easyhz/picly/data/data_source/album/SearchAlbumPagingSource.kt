package com.easyhz.picly.data.data_source.album

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.data.mapper.toAlbumItem
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SearchAlbumPagingSource(
    private val albumRepository: AlbumRepository,
    private val searchText: String,
): PagingSource<QuerySnapshot, AlbumItem>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, AlbumItem>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, AlbumItem> = try {
        val current = params.key ?: searchAlbum()
        val data = current.toObjects(Album::class.java).toAlbumItem()
        if (data.isEmpty()) {
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        } else {
            val lastVisibleProduct = current.documents.last()
            val nextPage = searchNextAlbum(lastVisibleProduct)

            LoadResult.Page(data = data, prevKey = null, nextKey = nextPage)
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private suspend fun searchAlbum() = withContext(Dispatchers.IO) {
        albumRepository.searchAlbums(searchText).get().await()
    }

    private suspend fun searchNextAlbum(lastVisibleProduct: DocumentSnapshot) = withContext(Dispatchers.IO) {
        albumRepository.searchAlbums(searchText).startAfter(lastVisibleProduct).get().await()
    }
}