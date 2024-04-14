package com.easyhz.picly.domain.model.album.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailImageSliderItem(
    val imageList: List<String>,
    val current: String
): Parcelable
