package com.easyhz.picly.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.easyhz.picly.databinding.FragmentSettingsBinding
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class SettingsFragment: Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        return binding.root
    }
}