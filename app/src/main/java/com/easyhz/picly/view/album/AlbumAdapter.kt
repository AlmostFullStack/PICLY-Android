package com.easyhz.picly.view.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemAlbumBinding
import com.easyhz.picly.domain.model.album.AlbumItem

class AlbumAdapter(): PagingDataAdapter<AlbumItem, AlbumAdapter.AlbumViewHolder>(differ) {
    private lateinit var mOnItemClickListener: OnItemClickListener
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        mOnItemClickListener = onItemClickListener
    }
    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                albumCardView.setOnClickListener { view ->
                    val pos = absoluteAdapterPosition
                    val item = getItem(pos)
                    if(pos != RecyclerView.NO_POSITION && item != null) {
                        mOnItemClickListener.onItemClick(view, item)
                    }
                }
                albumCardView.setOnLongClickListener { view ->
                    val pos = absoluteAdapterPosition
                    val item = getItem(pos)
                    if(pos != RecyclerView.NO_POSITION && item != null) {
                        fade.apply {
                            mOnItemClickListener.onLongClick(this, item)
                            visibility = View.VISIBLE
                            background = getDrawable(view.context, R.drawable.ripple_card_view)
                        }
                    }
                    true
                }
                linkButton.setOnClickListener { view ->
                    val pos = absoluteAdapterPosition
                    val item = getItem(pos)
                    if(pos != RecyclerView.NO_POSITION && item != null) {
                        mOnItemClickListener.onLinkClick(view, item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder = AlbumViewHolder(
        ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.binding.apply {
            data = currentItem
        }
    }

    companion object {
        private val differ = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem.documentId == newItem.documentId

            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean = oldItem == newItem

        }
    }

    interface OnItemClickListener{
        fun onItemClick(view: View, albumItem: AlbumItem)
        fun onLinkClick(view: View, albumItem: AlbumItem)
        fun onLongClick(fade: View, albumItem: AlbumItem)
    }
}