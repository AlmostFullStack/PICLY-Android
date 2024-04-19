package com.easyhz.picly

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.databinding.ActivityMainBinding
import com.easyhz.picly.domain.model.album.IncomingImages
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.exception.PiclyUncaughtExceptionHandler
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getFirstRun()
        setContentView(binding.root)
        uncaughtException()
    }

    private fun getFirstRun() {
        val isFirstRun = intent.getBooleanExtra("isFirstRun", true)
        initNavControllerManager(isFirstRun)
    }
    private fun initNavControllerManager(isFirstRun: Boolean) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavControllerManager.init(navHostFragment.navController, isFirstRun)
        if (!isFirstRun && UserManager.isLoggedIn()) getIncomingImages()
    }

    private fun getIncomingImages() {
        val incomingImages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("incomingImages", Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("incomingImages")
        }
        if (incomingImages.isNullOrEmpty()) return
        if (incomingImages.size > 10) {
            BlueSnackBar.make(binding.root, getString(R.string.over_selected)).show()
            return
        }
        NavControllerManager.navigateMainToUploadWithIncoming(IncomingImages().apply { addAll(incomingImages) })
    }

    private fun uncaughtException() {
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            defaultUncaughtExceptionHandler?.let { PiclyUncaughtExceptionHandler(it) }
        )
    }

}