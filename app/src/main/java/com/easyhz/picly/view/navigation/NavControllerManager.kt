package com.easyhz.picly.view.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.easyhz.picly.R
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.domain.model.album.IncomingImages
import com.easyhz.picly.view.MainFragmentDirections
import com.easyhz.picly.view.album.detail.AlbumDetailFragmentDirections
import com.easyhz.picly.view.album.upload.UploadFragmentDirections
import com.easyhz.picly.view.onboarding.OnboardingFragmentDirections
import com.easyhz.picly.view.settings.account.AccountFragmentDirections
import com.easyhz.picly.view.user.LoginFragmentDirections
import com.easyhz.picly.view.user.email.EmailLoginFragmentDirections
import com.easyhz.picly.view.user.email.SignUpFragmentDirections
import java.lang.ref.WeakReference

object NavControllerManager {
    private var navControllerRef: WeakReference<NavController>? = null
    val backStack: NavBackStackEntry?
        get() = navControllerRef?.get()?.previousBackStackEntry

    fun init(navController: NavController, isFirstRun: Boolean) {
        this.navControllerRef = WeakReference(navController)
        setStartDestination(isFirstRun)
    }

    /* 로그인 여부에 따른 startDestination 설정 */
    private fun setStartDestination(isFirstRun: Boolean) {
        val navController = navControllerRef?.get()
        val navGraph = navController?.navInflater?.inflate(R.navigation.nav_main)
        val startDestination = if (isFirstRun) {
            R.id.onboardingFragment
        } else if (UserManager.isLoggedIn()) {
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

    fun navigateOnboardingToLogin() {
        val action = OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
        navControllerRef?.get()?.navigate(action)
    }
    fun navigateLoginToLoginEmail() {
        val action = LoginFragmentDirections.actionLoginFragmentToEmailLoginFragment()
        navControllerRef?.get()?.navigate(action)
    }
    fun navigateLoginToMain() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
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
    fun navigateEmailSignUpToSignUp() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToAlbumFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateMainToUpload() {
        val action = MainFragmentDirections.actionMainFragmentToUploadFragment(null)
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateMainToUploadWithIncoming(incomingImage: IncomingImages) {
        val action = MainFragmentDirections.actionMainFragmentToUploadFragment(incomingImages = incomingImage)
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateMainToDetail(albumItem: AlbumItem) {
        val action = MainFragmentDirections.actionMainFragmentToAlbumDetailFragment(albumItem = albumItem)
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateDetailToBottomMenu(url: String) {
        val action = AlbumDetailFragmentDirections.actionAlbumDetailFragmentToDetailMenuBottomSheet(url)
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateUploadToMain() {
        val action = UploadFragmentDirections.actionUploadFragmentToMainFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateDetailBottomMenuToMain() {
        val action = AlbumDetailFragmentDirections.actionAlbumDetailFragmentToMainFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateSettingToOnboarding() {
        val action = MainFragmentDirections.actionMainFragmentToOnboardingFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateSettingToAccount() {
        val action = MainFragmentDirections.actionMainFragmentToAccountFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun navigateAccountToLogin() {
        val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
        navControllerRef?.get()?.navigate(action)
    }

    fun getNavController(): NavController {
        return checkNotNull(navControllerRef?.get()) { "NavController is not initialized" }
    }
}