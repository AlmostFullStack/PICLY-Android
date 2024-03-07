package com.easyhz.picly.data.mapper

import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.util.toDateFormat
import com.easyhz.picly.util.toDay

fun List<Album>.toAlbumItem(): List<AlbumItem> =  this.map {
    AlbumItem(
        regDate = it.creationTime.toDateFormat(),
        expireDate = it.expireTime.toDay(),
        imageCount = it.imageCount,
        imageSize = it.imageSizes,
        imageUrls = it.imageUrls,
        tags = it.tags.drop(1),
        thumbnailUrl = it.thumbnailUrl,
        viewCount = it.viewCount,
        mainTag = if (it.tags.isEmpty()) "" else it.tags[0],
        documentId = it.documentId
    )
}