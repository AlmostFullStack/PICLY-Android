package com.easyhz.picly.data.data_source.gallery

import com.easyhz.picly.data.data_source.BasePagingSource
import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.domain.repository.gallery.GalleryRepository

class GalleryFolderPagingSource(
    private val galleryRepository: GalleryRepository
): BasePagingSource<GalleryFolder>() {
    override suspend fun fetchData(page: Int, loadSize: Int): List<GalleryFolder> {
        return galleryRepository.fetchFolder(
            page = page,
            loadSize = loadSize
        )
    }

}