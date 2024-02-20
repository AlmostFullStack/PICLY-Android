package com.easyhz.pico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.easyhz.pico.databinding.ActivityMainBinding
import com.easyhz.pico.view.album.AlbumFragment
import com.easyhz.pico.view.album.AlbumViewModel
import com.easyhz.pico.view.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<AlbumViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        initDefaultFragment()
        setUpBottomNavigation()
        setUpToolbar()
        fetchAlbums()
    }

    private fun initDefaultFragment() {
        getFragment(R.id.page_album)?.let { replaceFragment(it) }
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            getFragment(menuItem.itemId)?.let {
                replaceFragment(it)
                setToolbarTitle(menuItem.itemId)
            }
            true
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, fragment).commit()
    }

    private fun getMenu(id: Int) = BottomNavigationItemType.entries.find { it.itemId == id }

    private fun getFragment(id: Int) = getMenu(id)?.fragment

    private fun setToolbarTitle(itemId: Int) {
        getMenu(itemId)?.let {
            binding.toolbarTitle.text = getString(it.title)
        }
    }

    private fun fetchAlbums() {
        viewModel.fetchAlbums()
    }

    enum class BottomNavigationItemType(val itemId : Int, val title: Int) {
        ALBUM(R.id.page_album, R.string.app_title) {
            override val fragment: Fragment
                get() = AlbumFragment()
        },
        SETTINGS(R.id.page_settings, R.string.app_settings_title) {
            override val fragment: Fragment
                get() = SettingsFragment()
        };
        abstract val fragment: Fragment
    }
}
