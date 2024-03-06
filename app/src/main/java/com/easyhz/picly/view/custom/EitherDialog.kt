package com.easyhz.picly.view.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.easyhz.picly.R
import com.easyhz.picly.databinding.DialogEitherBinding
import com.easyhz.picly.util.toPx

class EitherDialog private constructor(
    private val title: String,
    private val message: String,
    private val orientationType: Orientation
) : DialogFragment() {
    private var binding: DialogEitherBinding? = null
    private var positiveButtonClickAction: (() -> Unit)? = null
    private var negativeButtonClickAction: (() -> Unit)? = null

    companion object {
        fun instance(title: String, message: String, orientation: Orientation): EitherDialog {
            return EitherDialog(title, message, orientation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEitherBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        setDirection()
        binding?.apply {
            titleTextView.text = title
            messageTextView.text = message
            positiveButton.text = getString(R.string.delete)
            negativeButton.text = getString(R.string.cancel)
        }

        binding?.positiveButton?.let { setButtonClickListener(it, positiveButtonClickAction) }
        binding?.negativeButton?.let { setButtonClickListener(it, negativeButtonClickAction) }

        return binding?.root
    }

    private fun setButtonClickListener(button: View, onClick: (() -> Unit)?) {
        button.setOnClickListener {
            onClick?.invoke()
            dismiss()
        }
    }

    fun setPositiveButton(
        positiveButtonText: String,
        color: Int? = null,
        onClick: () -> Unit
    ): EitherDialog {
        this.positiveButtonClickAction = onClick
        binding?.positiveButton?.text = positiveButtonText
        color?.let { c ->
            binding?.positiveButton?.setTextColor(c)
        }
        return this
    }

    fun setNegativeButton(
        negativeButtonText: String,
        color: Int? = null,
        onClick: () -> Unit
    ): EitherDialog {
        this.negativeButtonClickAction = onClick
        binding?.negativeButton?.text = negativeButtonText
        color?.let { c ->
            binding?.negativeButton?.setTextColor(c)
        }
        return this
    }

    private fun setDirection() {
        binding?.apply {
            buttonLayout.apply {
                layoutParams.height = orientationType.height.toPx(requireActivity())
                orientation = orientationType.orientation
                if (orientationType == Orientation.VERTICAL) buttonLayout.reverse()
            }
            buttonDividerHorizontal.visibility = orientationType.horizontalVisibility
            buttonDividerVertical.visibility = orientationType.verticalVisibility
        }
    }

    private fun LinearLayout.reverse() {
        val views = mutableListOf<View>()

        for (i in 0 until childCount) {
            views.add(this.getChildAt(i))
        }
        views.reverse()
        this.removeAllViews()
        for (view in views) {
            this.addView(view)
        }
    }
}
enum class Orientation(
    val orientation: Int,
    val height: Int,
    val verticalVisibility: Int,
    val horizontalVisibility: Int
) {
    VERTICAL(
        orientation = LinearLayout.VERTICAL,
        height = 100,
        verticalVisibility = View.VISIBLE,
        horizontalVisibility = View.GONE
    ), HORIZONTAL(
        orientation = LinearLayout.HORIZONTAL,
        height = 52,
        verticalVisibility = View.GONE,
        horizontalVisibility = View.VISIBLE
    )
}

