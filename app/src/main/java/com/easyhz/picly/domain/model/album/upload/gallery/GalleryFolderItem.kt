package com.easyhz.picly.domain.model.album.upload.gallery

import android.net.Uri

data class GalleryFolderItem(
    val id: String,
    val folderName: String,
    val thumbnailUri: Uri,
    val count: Int
)