package com.easyhz.picly.view.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyhz.picly.R
import com.easyhz.picly.databinding.DialogEitherBinding

class EitherDialog private constructor(
    private val title: String,
    private val message: String,
) : DialogFragment() {
    private var binding: DialogEitherBinding? = null
    private var positiveButtonClickAction: (() -> Unit)? = null
    private var negativeButtonClickAction: (() -> Unit)? = null

    companion object {
        fun instance(title: String, message: String): EitherDialog {
            return EitherDialog(title, message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEitherBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

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
    onClick: () -> Unit
    ): EitherDialog {
        this.positiveButtonClickAction = onClick
        binding?.positiveButton?.text = positiveButtonText
        return this
    }

    fun setNegativeButton(
        negativeButtonText: String,
        onClick: () -> Unit
    ): EitherDialog {
        this.negativeButtonClickAction = onClick
        binding?.negativeButton?.text = negativeButtonText
        return this
    }
}