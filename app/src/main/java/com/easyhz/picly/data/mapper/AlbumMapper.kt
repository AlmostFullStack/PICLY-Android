package com.easyhz.picly.data.mapper

import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.util.toDateFormat
import com.easyhz.picly.util.toDay

fun List<Album>.toAlbumItem(): List<AlbumItem> =  this.map {
    AlbumItem(
        regDate = it.creationTime.toDateFormat(),
        expireDate = it.expireTime.toDay(),
        tag = it.tags[0],
        thumbnailUrl = it.thumbnailUrl,
        imageCount = it.imageCount
    )
}