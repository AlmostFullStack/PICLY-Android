package com.easyhz.picly.view.album.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.usecase.album.upload.UploadUseCase
import com.easyhz.picly.util.toFirebaseTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadViewModel
@Inject constructor(
    private val uploadUseCase: UploadUseCase
): ViewModel() {
    private val _tags = MutableLiveData<List<String>>()
    val tags : LiveData<List<String>>
        get() = _tags

    private var _selectedImageList = MutableLiveData<MutableList<GalleryImageItem>>()

    val selectedImageList: MutableLiveData<MutableList<GalleryImageItem>>
        get() = _selectedImageList

    init {
        _tags.value = emptyList()
    }

    fun writeAlbums(
        selectedImageList: List<GalleryImageItem>,
        expiredDate: String,
        expiredTime: String,
        onFailure: (String) -> Unit,
        onSuccess: (String) -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {
        val ownerId = UserManager.currentUser?.uid ?: run {
            return@launch
        }
        uploadUseCase.writeAlbums(
            ownerId = ownerId,
            tags = tags.value.orEmpty(),
            selectedImageList = selectedImageList,
            expireTime = "$expiredDate $expiredTime".toFirebaseTimestamp()
        ).catch { e ->
            e.localizedMessage?.let { onFailure(it) }
        }.collectLatest {
            withContext(Dispatchers.Main) {
                onSuccess(it)
            }
        }

    }

    fun addTag(tag: String) {
        _tags.value = _tags.value.orEmpty().toMutableList().apply {
            if (!contains(tag) && tag.isNotBlank()) {
                add(tag)
            }
        }
    }

    fun addSelectedImageList(value: List<GalleryImageItem>) {
        _selectedImageList.value = (_selectedImageList.value ?: mutableListOf()).apply {
            addAll(value)
            updatePositions()
        }
    }

    fun deleteSelectedImageList(value: GalleryImageItem) {
        _selectedImageList.value = _selectedImageList.value.orEmpty().toMutableList().apply { remove(value) }
    }

    fun setPosition() {
        _selectedImageList.value?.forEachIndexed { index, galleryImageItem ->
            galleryImageItem.position = index
        }
    }

    fun initSelectedImageList() {
        _selectedImageList.value = mutableListOf()
    }

    fun removeTag(tag: String) {
        _tags.value = _tags.value.orEmpty().toMutableList().apply {
            remove(tag)
        }
    }

    fun initTagList() {
        _tags.value = emptyList()
    }

    private fun MutableList<GalleryImageItem>.updatePositions() {
        this.forEachIndexed { index, galleryImageItem ->
            galleryImageItem.position = index
        }
    }
}