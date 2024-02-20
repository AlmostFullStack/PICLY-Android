package com.easyhz.pico.data.entity.album

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Album(
    @PropertyName("creationTime")
    val creationTime: Timestamp = Timestamp.now(),
    @PropertyName("expireTime")
    val expireTime: Timestamp = Timestamp.now(),
    @PropertyName("imageCount")
    val imageCount: Int = 0,
    @PropertyName("imageSizes")
    val imageSizes: List<ImageSize> = listOf(),
    @PropertyName("imageURLs")
    val imageUrls: List<String> = listOf(),
    @PropertyName("ownerID")
    val ownerId: String = "",
    @PropertyName("tags")
    val tags: List<String> = listOf(),
    @PropertyName("thumbnailURL")
    val thumbnailUrl: String = "",
    @PropertyName("viewCount")
    val viewCount: Int = 0
)
