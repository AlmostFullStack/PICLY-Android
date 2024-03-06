package com.easyhz.picly.view.settings

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat.getString
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import com.easyhz.picly.R
import com.easyhz.picly.databinding.ItemSettingBinding
import com.easyhz.picly.util.CommonAdapter
import com.easyhz.picly.util.toPx

class SettingsAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager
): CommonAdapter<Settings>(
    layoutId = R.layout.item_setting,
    differCallback = object : DiffUtil.ItemCallback<Settings>() {
        override fun areItemsTheSame(oldItem: Settings, newItem: Settings): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Settings, newItem: Settings): Boolean =
            oldItem == newItem
    }
) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = (holder.binding as ItemSettingBinding)
        val currentItem = differ.currentList[position]
        binding.apply {
            settingTextView.text = getString(context, currentItem.title)
            currentItem.icon?.let {
                settingIconView.visibility = View.VISIBLE
                settingIcon.setImageResource(it)
            } ?: run {
                linearLayout.layoutParams.height = 48.toPx(context)
            }
            currentItem.version?.let {
                versionLayout.visibility = View.VISIBLE
                currentVersionTextView.text = it.toString()
            }

            linearLayout.setOnClickListener {
                currentItem.onClick(fragmentManager)
            }
        }
    }
}