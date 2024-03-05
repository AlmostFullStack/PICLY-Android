package com.easyhz.picly.view.album

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easyhz.picly.R
import com.easyhz.picly.util.toDay

object AlbumBindingConversion {
    private const val HASH_TAG = "#"
    private const val D_DAY = "D-"
    private const val DAY = "DAY"
    private const val EXPIRATION = "만료"

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url)
            .error(R.drawable.default_image)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("albumTag")
    fun setTags(view: TextView, tag: String) {
       view.text = HASH_TAG.plus(tag)
    }

    @JvmStatic
    @BindingAdapter("expireDate")
    fun setExpireDate(view: TextView, expireMinute: Double) {
        val textColorResId = if (expireMinute <= 0) { R.color.errorColor } else { R.color.mainText }

        val expirationText = when {
            expireMinute <= 0 -> EXPIRATION
            expireMinute.toDay() == 0 -> D_DAY.plus(DAY)
            else -> D_DAY.plus(expireMinute.toDay())
        }

        view.text = expirationText
        view.setTextColor(ContextCompat.getColor(view.context, textColorResId))
    }

    @JvmStatic
    @BindingAdapter("multiplePicture")
    fun setMultiplePicture(view: ImageView, imageCount: Int) {
        view.visibility = if (imageCount > 1) View.VISIBLE else View.INVISIBLE
    }
}