package com.easyhz.picly.domain.model.album

import android.os.Parcelable
import com.easyhz.picly.data.entity.album.ImageSize
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumItem(
    val regDate: String,
    val expireDate: Double,
    val imageCount: Int,
    val imageSize: List<ImageSize>,
    val imageUrls: List<String>,
    val tags: List<String>,
    val thumbnailUrl: String,
    val viewCount: Int,
    val mainTag: String,
    val documentId: String,
): Parcelable
