package com.easyhz.picly.domain.model.album

import android.os.Parcelable
import com.easyhz.picly.data.entity.album.ImageSize
import com.easyhz.picly.domain.model.album.detail.DetailImageSliderItem
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
): Parcelable {

    fun toDetailImageItem(): List<DetailImageItem> = imageUrls.mapIndexed { index, url ->
        DetailImageItem(url, imageSize[index])
    }

    fun toDetailImageItem(current: String): DetailImageSliderItem = DetailImageSliderItem(
        imageList = imageUrls,
        current = current
    )

}