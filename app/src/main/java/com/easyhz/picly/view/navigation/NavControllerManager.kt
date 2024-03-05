package com.easyhz.picly.view.navigation

import androidx.navigation.NavController
import com.easyhz.picly.R
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.view.MainFragmentDirections
import com.easyhz.picly.view.album.upload.UploadFragmentDirections
import com.easyhz.picly.view.user.LoginFragmentDirections
import com.easyhz.picly.view.user.email.EmailLoginFragmentDirections
import java.lang.ref.WeakReference

object NavControllerManager {
    private var navControllerRef: WeakReference<NavController>? = null

    fun init(navController: NavController) {
        this.navControllerRef = WeakReference(navController)
        setStartDestination()
    }

    /* 로그인 여부에 따른 startDestination 설정 */
    private fun setStartDestination() {
        val navController = navControllerRef?.get()
        val navGraph = navController?.navInflater?.inflate(R.navigation.nav_main)
        val startDestination = if (UserManager.isLoggedIn()) {
            R.id.mainFragment
        } else {
            R.id.loginFragment
        }
        navGraph?.setStartDestination(startDestination)
        navController?.graph = navGraph ?: return
    }
    fun navigateToBack() {
        navControllerRef?.get()?.popBackStack()
    }

    fun navigateLoginToLoginEmail() {
        val action = LoginFragmentDirections.actionLoginFragmentToEmailLoginFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateEmailLoginToAlbum() {
        val action = EmailLoginFragmentDirections.actionEmailLoginFragmentToMainFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateEmailLoginToSignUp() {
        val action = EmailLoginFragmentDirections.actionEmailLoginFragmentToSignUpFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateMainToUpload() {
        val action = MainFragmentDirections.actionMainFragmentToUploadFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateUploadToMain() {
        val action = UploadFragmentDirections.actionUploadFragmentToMainFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun getNavController(): NavController {
        return checkNotNull(navControllerRef?.get()) { "NavController is not initialized" }
    }
}