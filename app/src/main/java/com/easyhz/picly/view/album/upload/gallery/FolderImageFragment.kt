package com.easyhz.picly.view.album.upload.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.databinding.FragmentGalleryFolderImageBinding
import com.easyhz.picly.util.showGalleryAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FolderImageFragment: BottomSheetDialogFragment() {
    private lateinit var binding: FragmentGalleryFolderImageBinding
    private lateinit var viewModel: GalleryBottomSheetViewModel
    private lateinit var galleryImageAdapter: GalleryImageAdapter
    private val args: FolderImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryFolderImageBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[GalleryBottomSheetViewModel::class.java]
        viewModel.setCurrentLocation(args.folderName)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setCurrentLocation(null)
    }
    private fun setUp() {
        setAlbumTitle()
        setGalleryImageRecyclerView()
        observeGalleryImageList()
        onClickBackButton()
    }

    private fun setAlbumTitle() {
        binding.albumTitle.text = args.folderName
    }

    private fun setGalleryImageRecyclerView() {
        galleryImageAdapter = GalleryImageAdapter()
        binding.galleryRecyclerView.apply {
            adapter = galleryImageAdapter
            layoutManager = GridLayoutManager(activity, GalleryFragment.GALLERY_IMAGE_GRID_VIEW)
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun observeGalleryImageList() = lifecycleScope.launch {
        viewModel.galleryImagePager.collectLatest {
            galleryImageAdapter.submitData(it)
        }
    }

    private fun onClickBackButton() {
        binding.backTextView.setOnClickListener {
            if (galleryImageAdapter.selectedImageList.isEmpty()) navigateUp()
            else showGalleryAlertDialog(requireActivity()) { navigateUp() }
        }
    }

    private fun navigateUp() {
        findNavController().navigateUp()
    }

}