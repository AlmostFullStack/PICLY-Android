package com.easyhz.picly.view.album.upload.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.databinding.GalleryBottomSheetBinding
import com.easyhz.picly.util.getScreenWidth
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryBottomSheetFragment:  BottomSheetDialogFragment() {
    private lateinit var binding: GalleryBottomSheetBinding
    private lateinit var viewModel: GalleryBottomSheetViewModel
    private lateinit var galleryImageAdapter: GalleryImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GalleryBottomSheetBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[GalleryBottomSheetViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        setRecyclerView()
        observeGalleryImageList()
        onClickAdd()
        onClickCancel()
    }

    private fun setRecyclerView() {
        galleryImageAdapter = GalleryImageAdapter(
            getScreenWidth(requireActivity()) / GRID_VIEW
        )
        binding.galleryRecyclerView.apply {
            adapter = galleryImageAdapter
            layoutManager = GridLayoutManager(activity, GRID_VIEW)
        }
    }

    private fun observeGalleryImageList() = lifecycleScope.launch {
        viewModel.pager.collectLatest {
            galleryImageAdapter.submitData(it)
        }
    }

    private fun onClickAdd() {
        binding.addTextView.setOnClickListener {
            viewModel.setSelectedImageList(galleryImageAdapter.selectedImageList)
            closeBottomSheet()
        }
    }

    private fun onClickCancel() {
        binding.cancelTextView.setOnClickListener {
            closeBottomSheet()
        }
    }

    private fun closeBottomSheet() {
        dismiss()
    }

    companion object {
        const val GRID_VIEW = 4
    }
}