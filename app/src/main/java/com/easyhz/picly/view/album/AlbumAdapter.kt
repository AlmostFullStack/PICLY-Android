package com.easyhz.picly.view.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.AlbumItemBinding
import com.easyhz.picly.domain.model.AlbumItem

class AlbumAdapter():RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root)


    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }

        override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder = AlbumViewHolder(
        AlbumItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.binding.data = differ.currentList[position]
    }

    fun setAlbumList(albumList: List<AlbumItem>) {
        differ.submitList(albumList)
    }

}