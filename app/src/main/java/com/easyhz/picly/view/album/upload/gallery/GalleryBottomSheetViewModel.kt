package com.easyhz.picly.view.album.upload.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.data.entity.gallery.GalleryImage
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.usecase.album.upload.GalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class GalleryBottomSheetViewModel
@Inject constructor(
    private val galleryUseCase: GalleryUseCase,
): ViewModel() {

    private var _selectedImageList = MutableLiveData<MutableList<GalleryImageItem>>(mutableListOf())

    val selectedImageList: MutableLiveData<MutableList<GalleryImageItem>>
        get() = _selectedImageList


    private var _isSelectedAlbum = MutableLiveData(false)

    val isSelectedAlbum: MutableLiveData<Boolean>
        get() = _isSelectedAlbum


    private val _currentLocation = MutableStateFlow<String?>(null)
    val currentLocation: StateFlow<String?> get() = _currentLocation

    @OptIn(ExperimentalCoroutinesApi::class)
    val galleryImagePager: Flow<PagingData<GalleryImage>> =
        currentLocation
            .flatMapLatest { location ->
                galleryUseCase.getGalleryImagePager(location)
            }
            .cachedIn(viewModelScope)

    val galleryFolderPager: Flow<PagingData<GalleryFolder>> =
        galleryUseCase.getGalleryFolderPager().cachedIn(viewModelScope)

    fun setSelectedImageList(value: MutableList<GalleryImageItem>) {
        _selectedImageList.value = mutableListOf<GalleryImageItem>().apply {
            addAll(_selectedImageList.value.orEmpty())
            addAll(value)
        }
    }

    fun deleteSelectedImageList(value: GalleryImageItem) {
        _selectedImageList.value = _selectedImageList.value.orEmpty().toMutableList().apply { remove(value) }
    }
    fun initSelectedImageList() {
        _selectedImageList.value = mutableListOf()
    }

    fun setIsSelectedAlbum(value: Boolean) {
        _isSelectedAlbum.value = value
    }

    fun setCurrentLocation(newLocation: String?) {
        _currentLocation.value = newLocation
    }
}