package com.easyhz.picly.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.distinctUntilChanged
import com.easyhz.picly.MainActivity
import com.easyhz.picly.databinding.ActivityStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        observeIsFirstRun()
        handleIncomingImage()
        setContentView(binding.root)
    }

    private fun observeIsFirstRun() {
        viewModel.isFirstRun.distinctUntilChanged().observe(this) { isFirstRun ->
            if (isFirstRun) {
                viewModel.updateFirstRunStatus()
            }
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
            }
            startMainActivity(isFirstRun)
        }
    }

    private fun startMainActivity(isFirstRun: Boolean) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isFirstRun" , isFirstRun)
        intent.putParcelableArrayListExtra("incomingImages", handleIncomingImage().toCollection(ArrayList()))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    /**
     *  Intent filter 로 들어온 사진을 처리 하는 함수
     */
    private fun handleIncomingImage(): List<Uri> {
        if (intent.type?.startsWith("image/") == false) return emptyList<Uri>()
        val imageUris = when(intent.action) {
            Intent.ACTION_SEND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOfNotNull(intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java))
                } else {
                    @Suppress("DEPRECATION")
                    listOfNotNull(intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM))
                }
            }
            Intent.ACTION_SEND_MULTIPLE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)?.toList()
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)?.toList()
                }
            }
            else -> emptyList<Uri>()
        } ?: emptyList<Uri>()
        return imageUris
    }
}