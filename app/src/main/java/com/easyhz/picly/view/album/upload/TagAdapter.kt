package com.easyhz.picly.view.album.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemTagBinding

class TagAdapter(
    private val onClickListener: (String) -> Unit
): RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    inner class TagViewHolder(val binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root)

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder = TagViewHolder(
        ItemTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )


    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            data = currentItem
            closeImageView.setOnClickListener {
                onClickListener(currentItem)
            }
        }
    }

    fun setTagList(tagList: List<String>) {
        differ.submitList(tagList.toList())
    }
}