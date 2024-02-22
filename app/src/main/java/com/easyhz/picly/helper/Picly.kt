package com.easyhz.picly.helper

import android.app.Application
import android.content.Intent
import com.easyhz.picly.MainActivity
import com.easyhz.picly.data.repository.user.UserManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

/**
 * 1. 처음 실행 판단 (온보딩)
 * 2. 로그인 여부 판단 (로그인 및 회원 가입)
 * 3. 메인 페이지 이동
 */
@HiltAndroidApp
class Picly: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}