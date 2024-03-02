package com.easyhz.picly.domain.model.album.upload.gallery

import android.net.Uri

data class GalleryImageItem(
    val id: Long,
    val path: String,
    val uri: Uri,
    val name: String,
    val regDate: String,
    val size: Long,
    val width: Long,
    val height: Long,
    var isSelected: Boolean = false,
    var position: Int
)
