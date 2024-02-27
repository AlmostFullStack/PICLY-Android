package com.easyhz.picly.data.data_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=ko)
 */
abstract class BasePagingSource<T: Any>: PagingSource<Int, T>() {

    abstract suspend fun fetchData(page: Int, loadSize: Int): List<T>
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition?.let {
        val closetPage = state.closestPageToPosition(it)
        closetPage?.prevKey?.plus(1) ?: closetPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val position = params.key ?: START_PAGE_INDEX
        val data = fetchData(position, params.loadSize)

        val isEnd = data.isEmpty()
        val prev = if(position == START_PAGE_INDEX) null else position - 1
        val next = if(isEnd) null else position + 1
        LoadResult.Page(data, prev, next)
    } catch (e: Exception) {
        Log.d(javaClass.simpleName, "LoadResult Error:: $e")
        LoadResult.Error(e)
    }

    companion object {
        const val START_PAGE_INDEX = 1
    }
}