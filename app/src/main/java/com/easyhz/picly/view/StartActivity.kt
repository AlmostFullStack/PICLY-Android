package com.easyhz.picly.view

import android.content.Intent
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
        startActivity(intent)
        finish()
    }
}