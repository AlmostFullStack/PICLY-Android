package com.easyhz.picly.view.settings.account

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyhz.picly.R
import com.easyhz.picly.util.toDateFormat
import com.google.firebase.Timestamp

object AccountBindingConversion {

    @JvmStatic
    @BindingAdapter("loginInfo")
    fun setLoginInfo(view: TextView, authProvider: String?) {
        if (authProvider != null) {
            view.text = String.format(view.context.getString(R.string.auth_provider), authProvider)
        }
    }

    @JvmStatic
    @BindingAdapter("creationTime")
    fun setCreationTime(view: TextView, creationTime: Timestamp?) {
        if (creationTime != null) {
            view.text = creationTime.toDateFormat(pattern = "yyyy.MM.dd")
        }
    }
}