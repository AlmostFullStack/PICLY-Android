package com.easyhz.picly.data.entity.album

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
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
    @get:PropertyName("imageURLs")
    @set:PropertyName("imageURLs")
    var imageUrls: List<String> = listOf(),
    @get:PropertyName("ownerID")
    @set:PropertyName("ownerID")
    var ownerId: String = "",
    @PropertyName("tags")
    val tags: List<String> = listOf(),
    @get:PropertyName("thumbnailURL")
    @set:PropertyName("thumbnailURL")
    var thumbnailUrl: String = "",
    @PropertyName("viewCount")
    val viewCount: Int = 0,
    @DocumentId
    val documentId: String = ""
)
