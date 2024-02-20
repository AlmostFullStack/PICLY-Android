package com.easyhz.pico.view.album

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easyhz.pico.R

object AlbumBindingConversion {
    private const val HASH_TAG = "#"
    private const val D_DAY = "D-"

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url)
            .error(R.drawable.ic_launcher_background)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("albumTag")
    fun setTags(view: TextView, tag: String) {
       view.text = HASH_TAG.plus(tag)
    }

    @JvmStatic
    @BindingAdapter("expireDate")
    fun setExpireDate(view: TextView, expireDate: Long) {
       view.text = D_DAY.plus(expireDate)
    }
}