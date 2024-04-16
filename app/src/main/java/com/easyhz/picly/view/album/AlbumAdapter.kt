package com.easyhz.picly.view.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemAlbumBinding
import com.easyhz.picly.domain.model.album.AlbumItem

class AlbumAdapter(
    private val onClickLinkButton: (AlbumItem) -> Unit,
    private val onLongClick: (AlbumItem, View) -> Unit,
    private val onClickListener: (AlbumItem) -> Unit,
): PagingDataAdapter<AlbumItem, AlbumAdapter.AlbumViewHolder>(differ) {
    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder = AlbumViewHolder(
        ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.binding.apply {
            data = currentItem
            albumCardView.setOnClickListener {
                onClickListener(currentItem)
            }
            albumCardView.setOnLongClickListener {
                fade.apply {
                    visibility = View.VISIBLE
                    onLongClick(currentItem, this)
                }
                true
            }
            linkButton.setOnClickListener {
                onClickLinkButton(currentItem)
            }
        }
    }

    companion object {
        private val differ = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.documentId == newItem.documentId

            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem

        }
    }
}