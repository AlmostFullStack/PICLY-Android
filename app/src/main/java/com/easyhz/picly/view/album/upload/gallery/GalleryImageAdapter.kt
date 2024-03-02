package com.easyhz.picly.view.album.upload.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.data.entity.gallery.GalleryImage
import com.easyhz.picly.data.entity.gallery.GalleryImage.Companion.toGalleryImageItem
import com.easyhz.picly.databinding.ItemGalleryImageBinding
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem

class GalleryImageAdapter(
): PagingDataAdapter<GalleryImage, GalleryImageAdapter.GalleryImageViewHolder>(differ) {
    val selectedImageList: MutableList<GalleryImageItem> = mutableListOf()

    inner class GalleryImageViewHolder(val binding: ItemGalleryImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageViewHolder = GalleryImageViewHolder(
        ItemGalleryImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: GalleryImageViewHolder, position: Int) {
        val currentItem = getItem(position)?.toGalleryImageItem(position)
        holder.binding.apply {
            data = currentItem
            val state = data?.isSelected
            val isSelectedInList = selectedImageList.any { it.position == position }

            if (isSelectedInList) {
                updateSelectionViews(state)
            }

            selectedTextView.text = (selectedImageList.indexOf(data).plus(1)).toString()

            galleryImageView.setOnClickListener {
                data?.let { galleryImage ->
                    updateSelectionViews(galleryImage.isSelected)

                    if (galleryImage.isSelected) {
                        handleSelectedImage(galleryImage)
                    } else {
                        handleDeselectedImage(galleryImage, position)
                    }
                }
            }
        }
    }

    private fun ItemGalleryImageBinding.updateSelectionViews(state: Boolean?) {
        val visibility = if (state == false) View.VISIBLE else View.GONE
        data?.isSelected = state == false
        selectedView.visibility = visibility
        selectedTextView.visibility = visibility
    }

    private fun ItemGalleryImageBinding.handleSelectedImage(galleryImage: GalleryImageItem) {
        selectedImageList.add(galleryImage)
        selectedTextView.text = (selectedImageList.indexOf(galleryImage).plus(1)).toString()
    }

    private fun handleDeselectedImage(galleryImage: GalleryImageItem, position: Int) {
        selectedImageList.removeIf { it.id == galleryImage.id }
        galleryImage.isSelected = false
        setOrderNumber()
    }
    private fun setOrderNumber() {
        selectedImageList.forEach {
            notifyItemChanged(it.position)
        }
    }

    companion object {
        private val differ = object: DiffUtil.ItemCallback<GalleryImage>() {
            override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
                return oldItem == newItem
            }
        }
    }
}