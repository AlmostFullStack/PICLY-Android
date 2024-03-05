package com.easyhz.picly.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class CommonAdapter<T>(
    private val layoutId: Int,
    private val differCallback: DiffUtil.ItemCallback<T>
): RecyclerView.Adapter<CommonAdapter<T>.ViewHolder>() {

    inner class ViewHolder(val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), layoutId, parent, false
        )
    )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { }

    fun setList(newList: List<T>) {
        differ.submitList(newList)
    }
}