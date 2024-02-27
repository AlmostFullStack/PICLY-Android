package com.easyhz.picly.data.data_source.gallery

import com.easyhz.picly.data.data_source.BasePagingSource
import com.easyhz.picly.data.entity.gallery.GalleryImage
import com.easyhz.picly.domain.repository.gallery.GalleryRepository

class GalleryImagePagingSource(
    private val galleryRepository: GalleryRepository,
    private val currentLocation: String?
): BasePagingSource<GalleryImage>() {
    override suspend fun fetchData(page: Int, loadSize: Int): List<GalleryImage> {
        return galleryRepository.fetchImages(
            page = page,
            loadSize = loadSize,
            currentLocation = currentLocation
        )
    }

}