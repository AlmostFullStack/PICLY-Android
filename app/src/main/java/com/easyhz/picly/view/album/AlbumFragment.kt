package com.easyhz.picly.view.album

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentAlbumBinding
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment: Fragment() {
    private lateinit var binding : FragmentAlbumBinding
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var viewModel: AlbumViewModel
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        setUp()

        return binding.root
    }

    private fun setUp() {
        setRecyclerView()
        observeAlbums()
        onclickFab()
    }

    private fun setRecyclerView() {
        albumAdapter = AlbumAdapter(onClickLinkButton = { onClickLinkButton(it) }) {
            NavControllerManager.navigateMainToDetail(it)
        }
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

    private fun onclickFab() {
        binding.addFab.setOnClickListener {
            NavControllerManager.navigateMainToUpload()
        }
    }

    private fun onClickLinkButton(albumItem: AlbumItem) {
        val clipData = ClipData.newPlainText(PICLY, albumItem.documentId.toPICLY())
        clipboardManager.setPrimaryClip(clipData)
        BlueSnackBar.make(binding.root, getString(R.string.link_copy)).show()
    }
}