package com.easyhz.picly.util

import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.easyhz.picly.R
import com.easyhz.picly.databinding.BlueSnackBarBinding
import com.google.android.material.snackbar.Snackbar

class BlueSnackBar(view: View, private val message: String) {

    companion object {
        const val BOTTOM_MARGIN = 100
        const val TEXT_SIZE = 20
        fun make(view: View, message: String) = BlueSnackBar(view, message)
    }

    private val context = view.context
    private val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    private val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout

    val binding: BlueSnackBarBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.blue_snack_bar, null, false)

    init {
        initView()
        initData()
    }
    private fun initView() {
        with(snackBarLayout) {
            removeAllViews()
            setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
            addView(binding.root, 0)
            snackBar.view.layoutParams = setLayoutParams()
        }
    }

    private fun initData() {
        binding.message.text = message
    }

    fun show() {
        snackBar.show()
    }


    private fun setLayoutParams(): FrameLayout.LayoutParams {
        val layoutParams = FrameLayout.LayoutParams(
            message.length * TEXT_SIZE.toPx(),
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.apply {
            bottomMargin = BOTTOM_MARGIN.toPx()
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        }
        return layoutParams
    }
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

}