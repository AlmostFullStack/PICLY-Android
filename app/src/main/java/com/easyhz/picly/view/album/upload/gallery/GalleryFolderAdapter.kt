package com.easyhz.picly.view.album.upload.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.data.entity.gallery.GalleryFolder.Companion.toGalleryFolderItem
import com.easyhz.picly.databinding.ItemGalleryFolderBinding
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryFolderItem

class GalleryFolderAdapter(
    private val onClick: (GalleryFolderItem) -> Unit
): PagingDataAdapter<GalleryFolder, GalleryFolderAdapter.GalleryFolderViewHolder>(differ) {
    inner class GalleryFolderViewHolder(val binding: ItemGalleryFolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryFolderViewHolder = GalleryFolderViewHolder(
        ItemGalleryFolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: GalleryFolderViewHolder, position: Int) {
        val currentItem = getItem(position)?.toGalleryFolderItem()
        holder.binding.apply {
            data = currentItem
            folderContainer.setOnClickListener {
                currentItem?.let(onClick)
            }
        }
    }

    companion object {
        private val differ = object: DiffUtil.ItemCallback<GalleryFolder>() {
            override fun areItemsTheSame(oldItem: GalleryFolder, newItem: GalleryFolder): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GalleryFolder, newItem: GalleryFolder): Boolean {
                return oldItem == newItem
            }
        }
    }
}