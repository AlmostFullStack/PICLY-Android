package com.easyhz.picly.view.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemAlbumBinding
import com.easyhz.picly.domain.model.album.AlbumItem

class AlbumAdapter(
    private val onClickLinkButton: (AlbumItem) -> Unit,
    private val onClickListener: (AlbumItem) -> Unit,
):RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)


    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<AlbumItem>() {
        override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }

        override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder = AlbumViewHolder(
        ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            data = currentItem
            albumCardView.setOnClickListener {
                onClickListener(currentItem)
            }
            linkButton.setOnClickListener {
                onClickLinkButton(currentItem)
            }
        }
    }

    fun setAlbumList(albumList: List<AlbumItem>) {
        differ.submitList(albumList)
    }

}