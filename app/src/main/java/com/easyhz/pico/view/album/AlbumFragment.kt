package com.easyhz.pico.view.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.pico.databinding.FragmentAlbumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment: Fragment() {
    private lateinit var binding : FragmentAlbumBinding
    private val albumAdapter = AlbumAdapter()
    private lateinit var viewModel: AlbumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]

        setUp()

        return binding.root
    }

    private fun setUp() {
        setupRecyclerView()
        observeAlbums()
    }

    private fun setupRecyclerView() {
        binding.albumRecyclerView.apply {
            adapter = albumAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun observeAlbums() {
        viewModel.albums.observe(viewLifecycleOwner) {
            albumAdapter.setAlbumList(it)
        }
    }
}