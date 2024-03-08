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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onStop() {
        super.onStop()
        viewModel.albums.removeObservers(viewLifecycleOwner)
    }

    private fun setUp() {
        setRecyclerView()
        observeAlbums()
        onclickFab()
        observeSearchText()
    }

    private fun setRecyclerView() {
        albumAdapter = AlbumAdapter(
            noResult = { isEmpty, s -> setNoResult(isEmpty, s) },
            onClickLinkButton = { onClickLinkButton(it) }
        ) {
            NavControllerManager.navigateMainToDetail(it)
        }
        binding.albumRecyclerView.apply {
            adapter = albumAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }
    }

    private fun observeAlbums() {
        viewModel.albums.observe(viewLifecycleOwner) { albums ->
            updateNoResultMessage(albums.isEmpty(), getString(R.string.no_data_text))
            albumAdapter.setAlbumList(albums)
            albumAdapter.originalList = albums
        }
    }

    private fun observeSearchText() {
        viewModel.searchText.observe(viewLifecycleOwner) {
            albumAdapter.filter.filter(it)
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

    private fun setNoResult(isEmpty: Boolean, s: String) {
        if (s.isEmpty() && albumAdapter.originalList.isEmpty()) updateNoResultMessage(true, getString(R.string.no_data_text))
        else updateNoResultMessage(isEmpty, getString(R.string.no_search_text))
    }

    private fun updateNoResultMessage(isEmpty: Boolean, message: String) {
        binding.noResultMessage.apply {
            text = message
            visibility = if (isEmpty) View.VISIBLE else View.GONE
        }
    }
}