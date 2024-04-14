package com.easyhz.picly.view.album.detail

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        val imageWidth = if (image.url.isNotBlank()) image.size.width else 938
        val imageHeight = if (image.url.isNotBlank()) image.size.height else 938
        if (viewWidth == -1 && view.width != 0) {
            viewWidth = view.width
        }
        val ratio = (imageWidth / viewWidth.toDouble())
        view.layoutParams.height = (imageHeight / ratio).toInt()
        Glide.with(view.context).load(image.url)
            .error(R.drawable.default_image)
            .fallback(R.drawable.default_image)
            .placeholder(R.color.collectionViewCellBackground)
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

    @JvmStatic
    @BindingAdapter("detailImageUrl")
    fun setDetailImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url)
            .error(R.drawable.default_image)
            .fallback(R.drawable.default_image)
            .placeholder(R.color.black)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    println("from : $dataSource")
                    return false
                }
            })
            .into(view)
    }
}