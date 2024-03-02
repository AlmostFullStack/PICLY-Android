package com.easyhz.picly.domain.usecase.album.upload

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.easyhz.picly.data.data_source.gallery.GalleryFolderPagingSource
import com.easyhz.picly.data.data_source.gallery.GalleryImagePagingSource
import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.data.entity.gallery.GalleryImage
import com.easyhz.picly.domain.repository.gallery.GalleryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryUseCase
@Inject constructor(
    private val galleryRepository: GalleryRepository
) {
    companion object {
        const val PAGE_SIZE = 50
    }
    /**
     * 갤러리 이미지 페이징을 위한 Pager 생성.
     *
     * @param currentLocation 현재 위치에 기반한 갤러리 이미지를 가져오기 위한 위치 정보
     * @return 페이징된 갤러리 이미지를 제공하는 Flow
     */
    fun getGalleryImagePager(currentLocation: String?): Flow<PagingData<GalleryImage>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GalleryImagePagingSource(
                    galleryRepository = galleryRepository,
                    currentLocation = currentLocation
                )
            }
        ).flow

    /**
     * 갤러리 폴더 페이징을 위한 Pager 생성.
     *
     * @return 페이징된 갤러리 폴더를 제공하는 Flow
     */
    fun getGalleryFolderPager(): Flow<PagingData<GalleryFolder>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GalleryFolderPagingSource(
                    galleryRepository = galleryRepository,
                )
            }
        ).flow

}