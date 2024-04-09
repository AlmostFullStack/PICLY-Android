package com.easyhz.picly.view.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.databinding.ItemAlbumBinding
import com.easyhz.picly.domain.model.album.AlbumItem

class AlbumAdapter(
    private val noResult: (Boolean , String) -> Unit,
    private val onClickLinkButton: (AlbumItem) -> Unit,
    private val onLongClick: (AlbumItem, View) -> Unit,
    private val onClickListener: (AlbumItem) -> Unit,
):RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>(), Filterable {
    var originalList: List<AlbumItem> = listOf()
    private val postFiler = PostFilter()

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

    fun setAlbumList(albumList: List<AlbumItem>) {
        differ.submitList(albumList)
    }

    override fun getFilter(): Filter = postFiler
    inner class  PostFilter: Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString().trim()
            val results = FilterResults()

            if (filterString.isEmpty()) {
                results.values = originalList
            } else {
                val filterList = originalList.filter { album ->
                    val tags = listOf(album.mainTag) + album.tags
                    tags.any { tag -> tag.contains(filterString) }
                }
                results.values = filterList
            }

            results.count = (results.values as List<AlbumItem>).size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList = results?.values as? List<AlbumItem> ?: emptyList()
            noResult(filteredList.isEmpty(), constraint.toString())
            differ.submitList(filteredList)
        }
    }
}