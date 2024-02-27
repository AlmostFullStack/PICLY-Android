package com.easyhz.picly.view.album.upload.gallery

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easyhz.picly.R

object GalleryImageBindingConversion {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImage(view: ImageView, uri: Uri) {
        Glide.with(view.context).load(uri)
            .error(R.drawable.default_image)
            .into(view)
    }
}