package com.easyhz.picly.view.album.detail

import androidx.recyclerview.widget.DiffUtil
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemDetailImageBinding
import com.easyhz.picly.domain.model.album.DetailImageItem
import com.easyhz.picly.util.CommonAdapter

class DetailImageAdapter():CommonAdapter<DetailImageItem>(
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
        binding.data = differ.currentList[position]
    }

}