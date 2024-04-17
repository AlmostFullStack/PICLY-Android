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

class AlbumPagingSource(
    private val albumRepository: AlbumRepository
): PagingSource<QuerySnapshot, AlbumItem>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, AlbumItem>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, AlbumItem> = try {
        val current = params.key ?: fetchAlbum()
        val data = current.toObjects(Album::class.java).toAlbumItem()
        if (data.isEmpty()) {
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        } else {
            val lastVisibleProduct = current.documents.last()
            val nextPage = fetchNextAlbum(lastVisibleProduct)

            LoadResult.Page(data = data, prevKey = null, nextKey = nextPage)
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    private suspend fun fetchAlbum() = withContext(Dispatchers.IO) {
        albumRepository.fetchAlbums().get().await()
    }

    private suspend fun fetchNextAlbum(lastVisibleProduct: DocumentSnapshot) = withContext(Dispatchers.IO) {
        albumRepository.fetchAlbums().startAfter(lastVisibleProduct).get().await()
    }
}