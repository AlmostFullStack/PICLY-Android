package com.easyhz.picly.view.album.detail

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easyhz.picly.R
import com.easyhz.picly.util.toDetailDay
import java.text.NumberFormat
import java.util.Locale

object AlbumDetailBindingConversion {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url)
            .error(R.drawable.default_image)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("detailExpireDate")
    fun setDetailExpireDate(view: TextView, expireMinute: Double) {
        val textColorResId = if (expireMinute <= 0) R.color.errorColor else R.color.mainText

        view.text = expireMinute.toDetailDay()
        view.setTextColor(ContextCompat.getColor(view.context, textColorResId))
    }

    @JvmStatic
    @BindingAdapter("viewCount")
    fun setViewCount(view: TextView, viewCount: Int) {
        view.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(viewCount)
    }
}