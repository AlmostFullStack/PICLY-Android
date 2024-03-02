package com.easyhz.picly.util

import android.app.AlertDialog
import android.content.Context
import com.easyhz.picly.R

fun showGalleryAlertDialog(context: Context, onContinue: () -> Unit){
    AlertDialog.Builder(context, R.style.DialogTheme)
        .setPositiveButton(R.string.gallery_alert_continue){
                dialogInterface, i ->
            onContinue()
            dialogInterface.dismiss()
        }
        .setNegativeButton(R.string.gallery_alert_cancel) {
                dialogInterface, _ ->
           dialogInterface.cancel()
        }
        .setCancelable(false)
        .setTitle(R.string.gallery_alert_title)
        .setMessage(R.string.gallery_alert_message)
        .show()
}