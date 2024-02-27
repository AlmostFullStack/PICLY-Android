package com.easyhz.picly.view.album.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UploadViewModel: ViewModel() {
    private val _tags = MutableLiveData<List<String>>()
    val tags : LiveData<List<String>>
        get() = _tags

    init {
        _tags.value = emptyList()
    }

    fun addTag(tag: String) {
        _tags.value = _tags.value.orEmpty().toMutableList().apply {
            if (!contains(tag) && tag.isNotBlank()) {
                add(tag)
            }
        }
    }

    fun removeTag(tag: String) {
        _tags.value = _tags.value.orEmpty().toMutableList().apply {
            remove(tag)
        }
    }
}