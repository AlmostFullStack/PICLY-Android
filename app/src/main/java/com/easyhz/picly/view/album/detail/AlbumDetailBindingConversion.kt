package com.easyhz.picly.view.album.detail

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.easyhz.picly.R
import com.easyhz.picly.domain.model.album.DetailImageItem
import com.easyhz.picly.util.getShimmerDrawable
import com.easyhz.picly.util.toDetailDay
import java.text.NumberFormat
import java.util.Locale

object AlbumDetailBindingConversion {
    private var viewWidth = -1

    @JvmStatic
    @BindingAdapter("detailImage")
    fun setDetailImage(view: ImageView, image: DetailImageItem) {
        val shimmerDrawable = getShimmerDrawable(view.context)
        val imageWidth = image.size.width
        val imageHeight = image.size.height
        if (viewWidth == -1 && view.width != 0) {
            viewWidth = view.width
        }
        val ratio = (imageWidth / viewWidth.toDouble())
        view.layoutParams.height = (imageHeight / ratio).toInt()
        Glide.with(view.context).load(image.url)
            .error(R.drawable.default_image)
            .fallback(R.drawable.default_image)
            .placeholder(shimmerDrawable)
            .transition(DrawableTransitionOptions.withCrossFade())
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