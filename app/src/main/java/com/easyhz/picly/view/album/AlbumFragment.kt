package com.easyhz.picly.view.album

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentAlbumBinding
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.haptic
import com.easyhz.picly.util.showAlertDialog
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumFragment: Fragment() {
    private lateinit var binding : FragmentAlbumBinding
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var viewModel: AlbumViewModel
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        loading = LoadingDialog(requireActivity())

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
        setRefresh()
        refresh()
    }

    private fun setRecyclerView() {
        albumAdapter = AlbumAdapter(
            noResult = { isEmpty, s -> setNoResult(isEmpty, s) },
            onClickLinkButton = { onClickLinkButton(it) },
            onLongClick = { albumItem, view ->  onLongClick(albumItem, view) }
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
            if (!binding.swipeRefresh.isRefreshing) return@observe
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                binding.swipeRefresh.isRefreshing = false
                binding.albumRecyclerView.smoothScrollToPosition(0)
            }
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

    private fun onLongClick(albumItem: AlbumItem, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            haptic(requireContext(), 50)
            delay(500)
            showAlertDialog(
                context = requireContext(),
                title= R.string.dialog_delete_title,
                message = R.string.dialog_delete_message,
                positiveButtonText = R.string.delete,
                onContinue = {
                    loading.show(true)
                    deleteAlbum(albumItem.documentId)
                },
                negativeButtonText = R.string.cancel,
                onCancel = { },
                style = R.style.DialogDeleteTheme
            )
            view.visibility = View.GONE
        }
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

    private fun setRefresh() {
        binding.swipeRefresh.apply {
            setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.collectionViewCellBackground))
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.highlightBlue))
        }

    }

    private fun refresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchAlbums()
            viewModel.setSwipe(true)
        }
    }

    private fun deleteAlbum(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            when(val result = viewModel.deleteAlbum(id)) {
                is AlbumResult.Success -> { viewModel.fetchAlbums() }
                is AlbumResult.Error -> onFailure(result.errorMessage)
            }
            loading.show(false)
        }
    }

    private fun onFailure(message: String) {
        BlueSnackBar.make(binding.root, message).show()
    }
}