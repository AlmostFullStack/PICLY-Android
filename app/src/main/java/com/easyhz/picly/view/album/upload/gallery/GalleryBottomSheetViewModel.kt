package com.easyhz.picly.view.album.upload.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.easyhz.picly.domain.model.album.upload.GalleryImageItem
import com.easyhz.picly.domain.usecase.album.upload.GalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryBottomSheetViewModel
@Inject constructor(
    private val galleryUseCase: GalleryUseCase,
): ViewModel() {

    private var _selectedImageList = MutableLiveData<MutableList<GalleryImageItem>>(mutableListOf())

    val selectedImageList: MutableLiveData<MutableList<GalleryImageItem>>
        get() = _selectedImageList


    var currentLocation: String? = null

    val pager = galleryUseCase.getGalleryImagePager(currentLocation)

    fun setSelectedImageList(value: MutableList<GalleryImageItem>) {
        _selectedImageList.value = mutableListOf<GalleryImageItem>().apply {
            addAll(_selectedImageList.value.orEmpty())
            addAll(value)
        }
    }

    fun initSelectedImageList() {
        _selectedImageList.value = mutableListOf()
    }
}