package com.easyhz.picly.view.album.detail

import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemDetailImageBinding
import com.easyhz.picly.domain.model.album.DetailImageItem
import com.easyhz.picly.util.CommonAdapter


class DetailImageAdapter(
    private val context: Context,
    private val onClick: (View, String) -> Unit
):CommonAdapter<DetailImageItem>(
    layoutId = R.layout.item_detail_image,
    differCallback = object: DiffUtil.ItemCallback<DetailImageItem>() {
        override fun areItemsTheSame(oldItem: DetailImageItem, newItem: DetailImageItem): Boolean = oldItem.url == newItem.url

        override fun areContentsTheSame(
            oldItem: DetailImageItem,
            newItem: DetailImageItem
        ): Boolean = oldItem == newItem

    }
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = (holder.binding as ItemDetailImageBinding)
        val currentItem = differ.currentList[position]
        binding.data = currentItem
//        imagePreload(currentItem)
        binding.detailImageView.apply {
            setOnClickListener {
                onClick(this, currentItem.url)
            }
            ViewCompat.setTransitionName(this, currentItem.url)
        }
    }

    private fun imagePreload(item: DetailImageItem) {
        Glide.with(context)
            .load(item.url)
            .override(item.size.width.toInt(), item.size.height.toInt())
            .transform(CenterCrop())
            .preload()
    }

}