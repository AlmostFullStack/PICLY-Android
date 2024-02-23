package com.easyhz.picly.util.user

import android.content.Context
import android.text.InputType
import androidx.core.content.ContextCompat.getString
import com.easyhz.picly.R
import com.easyhz.picly.databinding.UserFieldBinding
import com.google.android.material.textfield.TextInputLayout

fun setEmailField(context: Context, binding: UserFieldBinding) {
    binding.emailField.apply {
        textViewLabel.text = getString(context, R.string.email_text)
        editText.hint = getString(context, R.string.email_hint)
        editTextField.endIconMode = TextInputLayout.END_ICON_NONE
        editText.inputType = InputType.TYPE_CLASS_TEXT
    }
}

fun setPasswordField(context: Context, binding: UserFieldBinding) {
    binding.passwordField.apply {
        editTextField.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }
}