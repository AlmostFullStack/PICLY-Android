package com.easyhz.picly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.easyhz.picly.databinding.ActivityMainBinding
import com.easyhz.picly.view.MainViewModel
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        observeIsFirstRun()
        setContentView(binding.root)
    }

    private fun observeIsFirstRun() {
        viewModel.isFirstRun.observe(this) { isFirstRun ->
            println("제발 요.. $isFirstRun")
            initNavControllerManager(isFirstRun)
            if (isFirstRun) {
                viewModel.updateFirstRunStatus()
            }
        }
    }

    private fun initNavControllerManager(isFirstRun: Boolean) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavControllerManager.init(navHostFragment.navController, isFirstRun)
    }

}