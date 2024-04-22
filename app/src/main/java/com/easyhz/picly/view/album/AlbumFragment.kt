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
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.data.repository.album.AlbumRepositoryImpl.Companion.PAGE_SIZE
import com.easyhz.picly.databinding.FragmentAlbumBinding
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.AlbumItem
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.fadeOut
import com.easyhz.picly.util.haptic
import com.easyhz.picly.util.showAlertDialog
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumFragment: Fragment() {
    private var binding : FragmentAlbumBinding? = null
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var viewModel: AlbumViewModel
    private lateinit var sharedViewModel: SharedAlbumStateViewModel
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AlbumViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedAlbumStateViewModel::class.java]
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        loading = LoadingDialog(requireActivity())

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding?.noResultMessage?.visibility == View.VISIBLE) {
            binding?.noResultMessage?.visibility = View.GONE
        }
    }

    private fun setUp() {
        initSkeleton()
        setRecyclerView()
        fetchSearchAlbums()
        fetchAlbums()
        onclickFab()
        setRefresh()
        refresh()
        pagesUpdatedFlow()
        observeIsUpload()
        observeIsReselectedAlbumMenu()
        setOnAdapterClickListener()
    }

    private fun setRecyclerView() {
        albumAdapter = AlbumAdapter()
        binding!!.albumRecyclerView.apply {
            adapter = albumAdapter
            layoutManager = GridLayoutManager(activity, 2)
            setItemViewCacheSize((2 * PAGE_SIZE).toInt())
        }
    }

    private fun fetchAlbums() {
        lifecycle.coroutineScope.launch {
            viewModel.albumPager.collectLatest { albums ->
                albumAdapter.submitData(albums)
            }
        }
    }

    private fun fetchSearchAlbums() {
        lifecycle.coroutineScope.launch {
            viewModel.searchPager.collectLatest {
                if (viewModel.searchText.value.isNullOrEmpty()) return@collectLatest
                albumAdapter.submitData(it)
            }
        }
    }

    private fun setAlbums() {
        lifecycle.coroutineScope.launch {
            viewModel.refresh()
            if (binding?.swipeRefresh?.isRefreshing == false) return@launch
            delay(500)
            viewModel.setSwipe(false)
            binding?.swipeRefresh?.isRefreshing = false
            binding?.albumRecyclerView?.smoothScrollToPosition(0)
        }
    }

    private fun pagesUpdatedFlow() {
        lifecycle.coroutineScope.launch {
            albumAdapter.loadStateFlow.collectLatest {
                if (it.prepend.endOfPaginationReached) {
                    updateNoResultMessage()
                    hideSkeleton()
                    if (!viewModel.searchText.value.isNullOrEmpty()) { binding?.albumRecyclerView?.smoothScrollToPosition(0) }
                }
            }
        }
    }

    private fun onclickFab() {
        binding?.addFab?.setOnClickListener {
            NavControllerManager.navigateMainToUpload()
        }
    }

    private fun setOnAdapterClickListener() {
        albumAdapter.setOnItemClickListener(object: AlbumAdapter.OnItemClickListener {
            override fun onItemClick(view: View, albumItem: AlbumItem) {
                NavControllerManager.navigateMainToDetail(albumItem)
            }

            override fun onLinkClick(view: View, albumItem: AlbumItem) {
                onClickLinkButton(albumItem)
            }

            override fun onLongClick(fade: View, albumItem: AlbumItem) {
                onLongClickItem(albumItem = albumItem, view = fade)
            }
        })
    }

    private fun onClickLinkButton(albumItem: AlbumItem) {
        val clipData = ClipData.newPlainText(PICLY, albumItem.documentId.toPICLY())
        clipboardManager.setPrimaryClip(clipData)
        binding?.root?.let { BlueSnackBar.make(it, getString(R.string.link_copy)).show() }
    }

    private fun onLongClickItem(albumItem: AlbumItem, view: View) {
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

    private fun updateNoResultMessage() {
        val message = if (viewModel.searchText.value.isNullOrEmpty()) {
            getString(R.string.no_data_text)
        } else {
            getString(R.string.no_search_text)
        }

        binding?.noResultMessage?.apply {
            text = message
            visibility = if (albumAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun setRefresh() {
        binding?.swipeRefresh?.apply {
            setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.collectionViewCellBackground))
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.highlightBlue))
        }

    }

    private fun refresh() {
        binding?.swipeRefresh?.setOnRefreshListener {
            setAlbums()
            viewModel.setSwipe(true)
        }
    }

    private fun deleteAlbum(id: String) {
        CoroutineScope(Dispatchers.Main).launch {
            when(val result = viewModel.deleteAlbum(id)) {
                is AlbumResult.Success -> {
                    setAlbums()
                    if (!viewModel.searchText.value.isNullOrEmpty()) viewModel.setSearchText("")
                }
                is AlbumResult.Error -> onFailure(result.errorMessage)
            }
            loading.show(false)
            sharedViewModel.setIsUpload(true)
        }
    }

    private fun onFailure(message: String) {
        binding?.root?.let { BlueSnackBar.make(it, message).show() }
    }

    private fun observeIsUpload() {
        sharedViewModel.isUpload.observe(viewLifecycleOwner) {
            if (it) {
                lifecycle.coroutineScope.launch {
                    viewModel.refresh()
                    sharedViewModel.setIsUpload(false)
                    delay(500)
                    binding?.albumRecyclerView?.smoothScrollToPosition(0)
                }
            }
        }
    }

    private fun observeIsReselectedAlbumMenu() {
        viewModel.isReselectedAlbumMenu.observe(viewLifecycleOwner) { isReselected ->
            if (!isReselected) return@observe
            binding?.albumRecyclerView?.apply {
                if (computeVerticalScrollOffset() != 0) {
                    smoothScrollToPosition(0)
                }
            }
            viewModel.setIsReselectedAlbumMenu(false)
        }
    }

    private fun hideSkeleton() {
        if (albumAdapter.itemCount != 0 || binding?.noResultMessage?.visibility == View.VISIBLE) {
            binding?.skeletonLoading?.hideShimmer()
            binding?.skeletonLoading?.stopShimmer()
            binding?.skeletonLoading?.fadeOut()
        }
    }

    private fun initSkeleton() {
        if (viewModel.isFirst.value == true) {
            viewModel.refresh()
            binding?.skeletonLoading?.startShimmer()
        } else {
            binding?.skeletonLoading?.hideShimmer()
            binding?.skeletonLoading?.stopShimmer()
            binding?.skeletonLoading?.visibility = View.GONE
        }
    }
}