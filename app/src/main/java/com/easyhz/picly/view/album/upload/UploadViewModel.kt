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

    fun removeTag(tag: String) {
        _tags.value = _tags.value.orEmpty().toMutableList().apply {
            remove(tag)
        }
    }

    fun initTagList() {
        _tags.value = emptyList()
    }
}