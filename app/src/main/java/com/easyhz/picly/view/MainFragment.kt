package com.easyhz.picly.view

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentMainBinding
import com.easyhz.picly.util.toPx
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
        setSearchBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onPause() {
        super.onPause()
        resetSearchBar()
    }

    private fun setUp() {
        fetchAlbums()
        observeIsSwipe()
    }

    private fun setNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.page_album -> handlePageAlbum()
                R.id.page_settings -> handlePageSettings()
            }
        }
    }

    private fun handlePageAlbum() {
        binding.toolbar.searchField.visibility = View.VISIBLE
        setToolbarTitle(R.string.app_title, R.font.bayon)
    }

    private fun handlePageSettings() {
        resetSearchBar()
        binding.toolbar.searchField.visibility = View.GONE
        setToolbarTitle(R.string.app_settings_title, R.font.sf_pro_bold, 22F)
    }

    private fun setToolbarTitle(titleResId: Int, fontResId: Int, fontSize: Float = 36F) {
        binding.toolbar.toolbarTitle.apply {
            text = getString(titleResId)
            typeface = resources.getFont(fontResId)
            textSize = fontSize
        }
    }

    private fun fetchAlbums() {
        viewModel.fetchAlbums()
    }

    private fun observeIsSwipe() {
        viewModel.isSwipe.observe(viewLifecycleOwner) {
            if (it) {
                resetSearchBar()
                viewModel.setSwipe(false)
            }
        }
    }
    private fun setSearchBar() {
        binding.toolbar.apply {
            searchCancelButton.setOnClickListener {
                resetSearchBar()
            }

            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) expandSearchBar()
                else collapseSearchBar()
            }

            searchEditText.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.setSearchText(s.toString())
                }
            })
        }
    }

    private fun resetSearchBar() {
        binding.toolbar.apply {
            searchEditText.setText("")
            viewModel.setSearchText("")
            searchEditText.clearFocus()
            val layoutParams = ConstraintLayout.LayoutParams(112.toPx(requireContext()), searchEditText.height)
            layoutParams.apply {
                bottomToBottom = ConstraintSet.PARENT_ID
                topToTop = ConstraintSet.PARENT_ID
                endToEnd = ConstraintSet.PARENT_ID
            }
            searchField.layoutParams = layoutParams
            searchCancelButton.visibility = View.GONE
        }
    }

    private fun expandSearchBar() {
        binding.toolbar.apply {
            val layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, searchEditText.height)
            layoutParams.apply {
                bottomToBottom = ConstraintSet.PARENT_ID
                topToTop = ConstraintSet.PARENT_ID
            }
            searchField.layoutParams = layoutParams
            searchCancelButton.visibility = View.VISIBLE
        }
    }

    private fun collapseSearchBar() {
        val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(binding.toolbar.searchEditText.windowToken, 0)
    }
}