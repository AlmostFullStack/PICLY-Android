package com.easyhz.picly.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.usecase.album.FetchAlbumUseCase
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import com.easyhz.picly.domain.usecase.album.SearchAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
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

    val albumPager = fetchAlbumUseCase().flow
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    val searchPager = searchAlbumUseCase().flow.cachedIn(viewModelScope)

    private var searchJob: Job? = null

    private val _isFirst = MutableLiveData<Boolean>(true)
    val isFirst : LiveData<Boolean>
        get() = _isFirst

    suspend fun deleteAlbum(id: String): AlbumResult<String> = deleteAlbumUseCase(id)

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        fetchAlbumUseCase.setPagingSource()
    }

    fun setSearchText(value: String) {
        if (value.isEmpty() && _searchText.value?.isNotEmpty() == true) { refresh() }
        _searchText.value = value
        if (value.isEmpty()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            searchAlbumUseCase.searchText = value
            searchAlbumUseCase.setPagingSource()
        }
    }

    fun setSwipe(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _isSwipe.postValue(value)
    }

    fun setIsFirst(value: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _isFirst.postValue(value)
    }
}