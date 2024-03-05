package com.easyhz.picly.view.album.detail

import androidx.recyclerview.widget.DiffUtil
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemDetailTagBinding
import com.easyhz.picly.util.CommonAdapter

class DetailTagAdapter():CommonAdapter<String>(
    layoutId = R.layout.item_detail_tag,
    differCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = (holder.binding as ItemDetailTagBinding)
        binding.data = differ.currentList[position]
    }
}