package com.easyhz.picly.view.navigation

import androidx.navigation.NavController

// NavControllerManager.kt
object NavControllerManager {
    private var navController: NavController? = null

    fun init(navController: NavController) {
        this.navController = navController
    }

    fun navigateToBack() {
        navController?.popBackStack()
    }
    fun getNavController(): NavController {
        return checkNotNull(navController) { "NavController is not initialized" }
    }

    fun startLoginActivity() {
    }
}