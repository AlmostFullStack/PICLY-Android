package com.easyhz.picly.view.settings.account

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyhz.picly.util.toDateFormat
import com.google.firebase.Timestamp

object AccountBindingConversion {
    private const val AUTH_PROVIDER = "로 로그인"

    @JvmStatic
    @BindingAdapter("loginInfo")
    fun setLoginInfo(view: TextView, authProvider: String?) {
        if (authProvider != null) {
            view.text = authProvider.plus(AUTH_PROVIDER)
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