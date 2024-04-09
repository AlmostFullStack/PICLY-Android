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

fun showAlertDialog(
    context: Context,
    title: Int = R.string.gallery_alert_title,
    message: Int = R.string.gallery_alert_message,
    positiveButtonText: Int = R.string.gallery_alert_continue,
    onContinue: () -> Unit,
    negativeButtonText: Int = R.string.gallery_alert_cancel,
    onCancel:() -> Unit,
    style: Int = R.style.DialogTheme
){
    AlertDialog.Builder(context, style)
        .setPositiveButton(positiveButtonText){
                dialogInterface, i ->
            onContinue()
            dialogInterface.dismiss()
        }
        .setNegativeButton(negativeButtonText) {
                dialogInterface, _ ->
            onCancel()
            dialogInterface.cancel()
        }
        .setCancelable(false)
        .setTitle(title)
        .setMessage(message)
        .show()
}