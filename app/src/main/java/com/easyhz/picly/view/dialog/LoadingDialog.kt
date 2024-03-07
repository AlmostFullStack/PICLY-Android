package com.easyhz.picly.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.easyhz.picly.databinding.DialogLoadingBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LoadingDialog @Inject constructor(
    @ApplicationContext context: Context
): AlertDialog(context) {
    private var binding = DialogLoadingBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(binding.root)
    }

    fun show(isShowing: Boolean) {
        if (isShowing) show()
        else dismiss()
    }
}