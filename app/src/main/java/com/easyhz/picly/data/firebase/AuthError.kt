package com.easyhz.picly.data.firebase

import com.easyhz.picly.R

enum class AuthError(val messageId: Int) {
    ERROR_INVALID_CUSTOM_TOKEN(R.string.error_invalid_custom_token),
    ERROR_CUSTOM_TOKEN_MISMATCH(R.string.error_custom_token_mismatch),
    ERROR_INVALID_CREDENTIAL(R.string.error_invalid_credential), // 제공된 인증 자격 증명이 잘못되었거나 만료되었습니다.
    ERROR_INVALID_EMAIL(R.string.error_invalid_email),
    ERROR_WRONG_PASSWORD(R.string.error_wrong_password),
    ERROR_USER_MISMATCH(R.string.error_user_mismatch),
    ERROR_REQUIRES_RECENT_LOGIN(R.string.error_requires_recent_login),
    ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL(R.string.error_account_exists_with_different_credential),
    ERROR_EMAIL_ALREADY_IN_USE(R.string.error_email_already_in_use),
    ERROR_CREDENTIAL_ALREADY_IN_USE(R.string.error_credential_already_in_use),
    ERROR_USER_DISABLED(R.string.error_user_disabled),
    ERROR_USER_TOKEN_EXPIRED(R.string.error_user_token_expired),
    ERROR_USER_NOT_FOUND(R.string.error_user_not_found),
    ERROR_INVALID_USER_TOKEN(R.string.error_invalid_user_token),
    ERROR_OPERATION_NOT_ALLOWED(R.string.error_operation_not_allowed),
    ERROR_WEAK_PASSWORD(R.string.error_weak_password),
    ERROR_MISSING_EMAIL(R.string.error_missing_email),
    ERROR_UNKNOWN(R.string.error_unknown)
}

