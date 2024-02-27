package com.easyhz.picly.domain.repository.gallery

import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.data.entity.gallery.GalleryImage

interface GalleryRepository {
    fun fetchImages(
        page: Int,
        loadSize: Int,
        currentLocation: String? = null
    ) : MutableList<GalleryImage>

    fun fetchFolder(
        page: Int,
        loadSize: Int
    ) : MutableList<GalleryFolder>
}