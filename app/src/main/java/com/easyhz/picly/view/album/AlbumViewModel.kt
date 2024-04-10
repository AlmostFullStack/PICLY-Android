package com.easyhz.picly.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.mapper.toAlbumItem
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.domain.usecase.album.AlbumUseCase
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel
@Inject constructor(
    private val albumUseCase: AlbumUseCase,
    private val deleteAlbumUseCase: DeleteAlbumUseCase
):ViewModel() {
    private val albumsLiveData = MutableLiveData<List<AlbumItem>>()
    val albums : LiveData<List<AlbumItem>>
        get() = albumsLiveData

    private val _searchText = MutableLiveData<String>()
    val searchText : LiveData<String>
        get() = _searchText

    private val _isSwipe = MutableLiveData<Boolean>(false)
    val isSwipe : LiveData<Boolean>
        get() = _isSwipe

    fun fetchAlbums() = viewModelScope.launch {
        albumUseCase().distinctUntilChanged().collectLatest {
            albumsLiveData.value = it.toAlbumItem()
        }
    }

    suspend fun deleteAlbum(id: String): AlbumResult<String> = deleteAlbumUseCase(id)

    fun setSearchText(value: String) {
        _searchText.value = value
    }

    fun setSwipe(value: Boolean) {
        _isSwipe.value = value
    }
}