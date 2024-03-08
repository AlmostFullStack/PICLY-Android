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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getFirstRun()
        setContentView(binding.root)
    }

    private fun getFirstRun() {
        val isFirstRun = intent.getBooleanExtra("isFirstRun", true)
        initNavControllerManager(isFirstRun)
    }
    private fun initNavControllerManager(isFirstRun: Boolean) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavControllerManager.init(navHostFragment.navController, isFirstRun)
    }

}