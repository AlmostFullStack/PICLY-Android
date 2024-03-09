package com.easyhz.picly.helper

import android.app.Application
import com.easyhz.picly.data.firebase.AnalyticsManager
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
        AnalyticsManager.init(this)
    }
}

/**
 * - TERMS_OF_SERVICE_URL: 이용약관
 * - PRIVACY_POLICY_URL: 개인정보 처리방침
 * - OPENSOURCE_LICENSE_URL: 오픈소스 라이센스
 * - DEVELOPER_URL: 개발자 정보
 */
const val TERMS_OF_SERVICE_URL = "https://jdeoks.notion.site/5cc8688a9432444eaad7a8fdc4e4e38a"
const val PRIVACY_POLICY_URL = "https://jdeoks.notion.site/bace573d0a294bdeae4a92464448bcac"
const val OPENSOURCE_LICENSE_URL = "https://jdeoks.notion.site/Android-c196bf1e4bf649d79473061000ebd4cf"
const val DEVELOPER_URL ="https://jdeoks.notion.site/a747b302e36f4c369496e7372768d685"