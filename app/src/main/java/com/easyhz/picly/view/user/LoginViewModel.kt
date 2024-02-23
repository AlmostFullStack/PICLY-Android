package com.easyhz.picly.view.user

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.easyhz.picly.R
import com.easyhz.picly.domain.usecase.user.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val loginUseCase: LoginUseCase
):ViewModel() {

    fun addClickableLink(
        context: Context,
        spannableString: SpannableString,
        loginWarningText: String,
        linkText: String,
        clickAction: () -> Unit
    ) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickAction.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(context, R.color.highlightBlue) // 링크 색상 설정
            }
        }

        val startIndex = loginWarningText.indexOf(linkText)
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + linkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}