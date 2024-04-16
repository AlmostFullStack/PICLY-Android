package com.easyhz.picly.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.domain.usecase.album.FetchAlbumUseCase
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import com.easyhz.picly.domain.usecase.album.SearchAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel
@Inject constructor(
    private val fetchAlbumUseCase: FetchAlbumUseCase,
    private val searchAlbumUseCase: SearchAlbumUseCase,
    private val deleteAlbumUseCase: DeleteAlbumUseCase,
):ViewModel() {
    private val _searchText = MutableLiveData<String>()
    val searchText : LiveData<String>
        get() = _searchText

    private val _isSwipe = MutableLiveData<Boolean>(false)
    val isSwipe : LiveData<Boolean>
        get() = _isSwipe

    val albumPager = fetchAlbumUseCase().flow.cachedIn(viewModelScope)

    val searchPager = searchAlbumUseCase().flow.cachedIn(viewModelScope)

    private var searchJob: Job? = null

    suspend fun deleteAlbum(id: String): AlbumResult<String> = deleteAlbumUseCase(id)

    fun refresh() {
        fetchAlbumUseCase.setPagingSource()
    }

    fun setSearchText(value: String) {
        _searchText.value = value
        if (value.isEmpty()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            searchAlbumUseCase.searchText = value
            searchAlbumUseCase.setPagingSource()
        }
    }

    fun setSwipe(value: Boolean) {
        _isSwipe.value = value
    }
}