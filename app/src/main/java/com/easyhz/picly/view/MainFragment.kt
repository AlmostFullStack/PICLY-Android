package com.easyhz.picly.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentMainBinding
import com.easyhz.picly.view.album.AlbumViewModel

class MainFragment:Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: AlbumViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]
        setNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }
    private fun setUp() {
        fetchAlbums()
    }

    private fun setNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.page_album -> setToolbarTitle(R.string.app_title, R.font.bayon)
                R.id.page_settings -> setToolbarTitle(R.string.app_settings_title, R.font.sf_pro_bold)
            }
        }
    }

    private fun setToolbarTitle(titleResId: Int, fontResId: Int) {
        binding.toolbar.toolbarTitle.apply {
            text = getString(titleResId)
            typeface = resources.getFont(fontResId)
        }
    }

    private fun fetchAlbums() {
        viewModel.fetchAlbums()
    }
}