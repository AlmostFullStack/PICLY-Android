package com.easyhz.picly.view.album.upload.gallery

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easyhz.picly.R

object GalleryImageBindingConversion {
    @JvmStatic
    @BindingAdapter("galleryUrl")
    fun setImage(view: ImageView, uri: Uri) {
        Glide.with(view.context).load(uri)
            .error(R.drawable.default_image)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("count")
    fun setCount(view: TextView, count: Int) {
        view.text = count.toString()
    }
}