package com.easyhz.picly.view.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedAlbumStateViewModel: ViewModel() {
    private val _isUpload = MutableLiveData<Boolean>(false)
    val isUpload : LiveData<Boolean>
        get() = _isUpload

    fun setIsUpload(value: Boolean) {
        _isUpload.value = value
    }
}