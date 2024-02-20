package com.easyhz.pico.data.mapper

import com.easyhz.pico.data.entity.album.Album
import com.easyhz.pico.domain.model.AlbumItem
import com.easyhz.pico.util.toDateFormat
import com.easyhz.pico.util.toDay

fun List<Album>.toAlbumItem(): List<AlbumItem> =  this.map {
    AlbumItem(
        regDate = it.creationTime.toDateFormat(),
        expireDate = it.expireTime.toDay(),
        tag = it.tags[0],
        thumbnailUrl = it.thumbnailUrl
    )
}