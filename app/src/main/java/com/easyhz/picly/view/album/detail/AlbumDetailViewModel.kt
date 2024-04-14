package com.easyhz.picly.view.album.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlbumDetailViewModel: ViewModel() {
    private val _scrollPosition = MutableLiveData<Int>()
    val scrollPosition: LiveData<Int> = _scrollPosition

    fun setScrollPosition(position: Int) {
        _scrollPosition.value = position
    }
}