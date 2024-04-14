package com.easyhz.picly.view.album.detail.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemDetailImageSliderBinding
import com.easyhz.picly.domain.model.album.detail.DetailImageSliderItem

class ImageSliderAdapter(
    private val images: DetailImageSliderItem,
    private val onLoaded: (String) -> Unit,
    private val onChangeScale: (Float) -> Unit,
): RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(val binding: ItemDetailImageSliderBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder = ImageViewHolder(
        ItemDetailImageSliderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = images.imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = images.imageList[position]
        holder.binding.apply {
//            imageUrl = currentItem
            ViewCompat.setTransitionName(detailImageView, currentItem)
            detailImageView.apply {
                setImage(this.context, currentItem)
                setScaleLevels(MINIMUM_SCALE, MEDIUM_SCALE, MAXIMUM_SCALE) // 배율 설정
                setOnScaleChangeListener { _, _, _ ->
                    onChangeScale(scale) // 확대 시 스크롤 방지 콜백
                }
                setAllowParentInterceptOnEdge(scale == 1.0f) // 축소 할 때 스크롤 방지
            }
        }
    }

    private fun ImageView.setImage(context: Context, currentItem: String) {
        Glide.with(context).load(currentItem)
            .error(R.drawable.default_image)
            .fallback(R.drawable.default_image)
            .placeholder(R.color.black)
            .listener(glideListener(currentItem))
            .into(this)
    }

    private fun glideListener(currentItem: String) = object : RequestListener<Drawable> {
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
            onLoaded(currentItem)
            return false
        }
    }

    companion object {
        const val MINIMUM_SCALE = 1.0F
        const val MEDIUM_SCALE = 2.0F
        const val MAXIMUM_SCALE = 5.0F
    }
}