package com.easyhz.picly.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.DialogEitherBinding
import com.easyhz.picly.util.toPx

class EitherDialog private constructor(
    private val title: String,
    private val message: String,
    private val orientationType: Orientation
) : DialogFragment() {
    private var binding: DialogEitherBinding? = null
    private var mPositiveButtonText: String = ""
    private var mPositiveButtonColor: Int? = null
    private var positiveButtonClickAction: (() -> Unit)? = null

    private var mNegativeButtonColor: Int? = null
    private var mNegativeButtonText: String = ""
    private var negativeButtonClickAction: (() -> Unit)? = null

    companion object {
        fun instance(title: String, message: String, orientation: Orientation): EitherDialogBuilder {
            return EitherDialogBuilder(title, message, orientation)
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

            positiveButton.apply {
                text = mPositiveButtonText
                mPositiveButtonColor?.let {
                    setTextColor(it)
                }
                setButtonClickListener(this, positiveButtonClickAction)
            }

            negativeButton.apply {
                text = mNegativeButtonText
                mNegativeButtonColor?.let {
                    setTextColor(it)
                }
                setButtonClickListener(this, negativeButtonClickAction)
            }
        }

        return binding?.root
    }

    private fun setButtonClickListener(button: View, onClick: (() -> Unit)?) {
        button.setOnClickListener {
            onClick?.invoke()
            dismiss()
        }
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

    class EitherDialogBuilder(
        private val title: String,
        private val message: String,
        private val orientationType: Orientation
    ) {
        private var positiveButtonText: String = ""
        private var positiveButtonColor: Int? = null
        private var positiveButtonAction: (() -> Unit)? = null

        private var negativeButtonText: String = ""
        private var negativeButtonColor: Int? = null
        private var negativeButtonAction: (() -> Unit)? = null

        fun setPositiveButton(
            text: String,
            color: Int? = null,
            onClick: () -> Unit
        ): EitherDialogBuilder {
            positiveButtonText = text
            positiveButtonColor = color
            positiveButtonAction = onClick
            return this
        }

        fun setNegativeButton(
            text: String,
            color: Int? = null,
            onClick: () -> Unit
        ): EitherDialogBuilder {
            negativeButtonText = text
            negativeButtonColor = color
            negativeButtonAction = onClick
            return this
        }

        fun show(fragmentManager: FragmentManager): EitherDialog {
            val dialog = EitherDialog(title, message, orientationType)
            dialog.apply {
                mPositiveButtonText = positiveButtonText
                mPositiveButtonColor = positiveButtonColor
                positiveButtonClickAction = positiveButtonAction

                mNegativeButtonText = negativeButtonText
                mNegativeButtonColor = negativeButtonColor
                negativeButtonClickAction = negativeButtonAction
            }

            dialog.show(fragmentManager, dialog.tag)
            return dialog
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

