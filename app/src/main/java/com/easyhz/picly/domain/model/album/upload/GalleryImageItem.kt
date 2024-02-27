package com.easyhz.picly.domain.model.album.upload

import android.net.Uri

data class GalleryImageItem(
    val id: Long,
    val path: String,
    val uri: Uri,
    val name: String,
    val regDate: String,
    val size: Int,
    var isSelected: Boolean = false,
    var position: Int
)
