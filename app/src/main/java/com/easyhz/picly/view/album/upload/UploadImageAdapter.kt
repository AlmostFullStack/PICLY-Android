package com.easyhz.picly.view.album.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemAddBinding
import com.easyhz.picly.databinding.ItemImageBinding
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem

class UploadImageAdapter(
    private val onClickAdd: () -> Unit,
    private val onClickListener: (GalleryImageItem) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class UploadImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root)

    inner class AddViewHolder(val binding: ItemAddBinding) : RecyclerView.ViewHolder(binding.root)

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<GalleryImageItem>() {
        override fun areItemsTheSame(
            oldItem: GalleryImageItem,
            newItem: GalleryImageItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: GalleryImageItem,
            newItem: GalleryImageItem
        ): Boolean = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ITEM_IMAGE -> UploadImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else -> AddViewHolder(ItemAddBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UploadImageViewHolder) {
            val currentItem = differ.currentList[position]
            holder.binding.apply {
                data = currentItem
                deleteButton.setOnClickListener {
                    onClickListener(currentItem)
                }
            }
        } else if (holder is AddViewHolder) {
            holder.binding.addImage.setOnClickListener {
                onClickAdd()
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size + 1

    override fun getItemViewType(position: Int): Int = if(position == differ.currentList.size) ITEM_ADD else ITEM_IMAGE

    fun setUploadImageList(uploadImageList: List<GalleryImageItem>) {
        differ.submitList(uploadImageList.toList())
    }

    companion object {
        const val ITEM_IMAGE = 0
        const val ITEM_ADD = 1
    }

}