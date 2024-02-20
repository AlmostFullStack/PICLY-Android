package com.easyhz.pico.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.pico.data.mapper.toAlbumItem
import com.easyhz.pico.domain.model.AlbumItem
import com.easyhz.pico.domain.usecase.album.AlbumUseCase
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