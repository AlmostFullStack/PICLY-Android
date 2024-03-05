package com.easyhz.picly.data.entity.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageSize(
    val height: Long = 0,
    val width: Long = 0
): Parcelable
