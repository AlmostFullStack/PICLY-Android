package com.easyhz.picly.domain.model.album

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class IncomingImages: ArrayList<Uri>(), Parcelable
