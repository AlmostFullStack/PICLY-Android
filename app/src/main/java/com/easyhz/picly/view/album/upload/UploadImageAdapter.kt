package com.easyhz.picly.view.album.upload

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemImageBinding
import com.easyhz.picly.domain.model.album.upload.GalleryImageItem

class UploadImageAdapter(
    private val onClickListener: (String) -> Unit
): RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder>() {

    inner class UploadImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadImageViewHolder = UploadImageViewHolder(
        ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: UploadImageViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.binding.apply {
            data = currentItem
        }
    }

    fun setUploadImageList(uploadImageList: List<GalleryImageItem>) {
        println("adapter $uploadImageList")
        differ.submitList(uploadImageList.toList())
    }


}