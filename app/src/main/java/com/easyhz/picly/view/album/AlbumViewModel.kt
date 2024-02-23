package com.easyhz.picly.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.mapper.toAlbumItem
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.domain.usecase.album.AlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AlbumViewModel
@Inject constructor(
    private val albumUseCase: AlbumUseCase
):ViewModel() {
    private val albumsLiveData = MutableLiveData<List<AlbumItem>>()
    val albums : LiveData<List<AlbumItem>>
        get() = albumsLiveData

    fun fetchAlbums() = viewModelScope.launch {
        albumUseCase().collectLatest {
            albumsLiveData.value = it.toAlbumItem()
        }
    }
}