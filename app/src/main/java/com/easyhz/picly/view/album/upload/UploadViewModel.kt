package com.easyhz.picly.view.album.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.usecase.album.upload.UploadUseCase
import com.easyhz.picly.util.toFirebaseTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
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

    suspend fun writeAlbums(
        selectedImageList: List<GalleryImageItem>,
        expiredDate: String,
        expiredTime: String
    ): AlbumResult<String> {
        val ownerId = UserManager.currentUser?.uid ?: return AlbumResult.Error("오류가 발생했습니다.")
        return uploadUseCase.writeAlbums(
            ownerId = ownerId,
            tags = tags.value.orEmpty(),
            selectedImageList = selectedImageList,
            expireTime = "$expiredDate $expiredTime".toFirebaseTimestamp()
        )
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