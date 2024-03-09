package com.easyhz.picly.view.album.upload.gallery

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentGalleryBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.showGalleryAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class GalleryFragment:  BottomSheetDialogFragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var viewModel: GalleryBottomSheetViewModel
    private lateinit var galleryImageAdapter: GalleryImageAdapter
    private lateinit var galleryFolderAdapter: GalleryFolderAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[GalleryBottomSheetViewModel::class.java]
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onResume() {
        super.onResume()
        setResume()
    }
    override fun onPause() {
        super.onPause()
        viewModel.setIsSelectedAlbum(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setIsSelectedAlbum(false)
    }

    private fun setUp() {
        setGalleryImageAdapter()
        setGalleryFolderAdapter()
        observeGalleryImageList()
        observeGalleryFolderList()
        observeIsSelectedAlbum()
        onClickAlbum()
        onClickImage()
        onClickAdd()
        onClickCancel()
    }

    private fun setGalleryImageAdapter() {
        galleryImageAdapter = GalleryImageAdapter { overSelected() }
        setGalleryImageRecyclerView()
    }
    private fun setGalleryFolderAdapter() {
        galleryFolderAdapter = GalleryFolderAdapter {
            if (galleryImageAdapter.selectedImageList.isEmpty()) navigateToFolder(it.folderName)
            else showGalleryAlertDialog(requireActivity()) { navigateToFolder(it.folderName) }
        }
    }

    private fun setGalleryImageRecyclerView() {
        binding.galleryFolderRecyclerView.visibility = View.GONE
        binding.galleryImageRecyclerView.apply {
            visibility = View.VISIBLE
            adapter = galleryImageAdapter
            layoutManager = GridLayoutManager(activity, GALLERY_IMAGE_GRID_VIEW)
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun setGalleryFolderRecyclerView() {
        binding.galleryImageRecyclerView.visibility = View.GONE
        binding.galleryFolderRecyclerView.apply {
            visibility = View.VISIBLE
            adapter = galleryFolderAdapter
            layoutManager = GridLayoutManager(activity, GALLERY_FOLDER_GRID_VIEW)
        }
    }

    private fun observeGalleryImageList() = lifecycleScope.launch {
        viewModel.galleryImagePager.collectLatest {
            galleryImageAdapter.submitData(it)
        }
    }

    private fun observeGalleryFolderList() = lifecycleScope.launch {
        viewModel.galleryFolderPager.collectLatest {
            galleryFolderAdapter.submitData(it)
        }
    }

    private fun observeIsSelectedAlbum() {
        viewModel.isSelectedAlbum.observe(viewLifecycleOwner) { isSelectedAlbum ->
            animateSelectedView(isSelectedAlbum)
            setRecyclerViewType(isSelectedAlbum)
        }
    }

    private fun onClickAlbum() {
        binding.galleryToggleButton.albumToggleTextView.apply {
            setOnClickListener {
                viewModel.setIsSelectedAlbum(true)
            }
        }
    }

    private fun onClickImage() {
        binding.galleryToggleButton.imageToggleTextView.apply {
            setOnClickListener {
                viewModel.setCurrentLocation(null)
                viewModel.setIsSelectedAlbum(false)
            }
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
        (parentFragment?.parentFragment as BottomSheetDialogFragment).dismiss()
    }

    private fun setRecyclerViewType(isSelectedAlbum: Boolean) {
        if (isSelectedAlbum && binding.galleryFolderRecyclerView.visibility == View.GONE) {
            setGalleryFolderRecyclerView()
        } else if (!isSelectedAlbum && binding.galleryImageRecyclerView.visibility == View.GONE) {
            setGalleryImageRecyclerView()
        }
    }

    private fun animateSelectedView(isSelectedAlbum: Boolean) {
        binding.galleryToggleButton.apply {
            val translationX = if (isSelectedAlbum) albumToggleTextView.width.toFloat() else 0f
            val translationAnimator = ObjectAnimator.ofFloat(selectedView, "translationX", translationX)
            translationAnimator.duration = 300
            translationAnimator.start()
        }
    }

    private fun setResume() {
        if (viewModel.isSelectedAlbum.value == false) return
        binding.galleryToggleButton.root.apply {
            viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        animateSelectedView(true)
                        viewTreeObserver.removeOnPreDrawListener(this)
                        return true
                    }
                }
            )
        }
    }

    private fun overSelected() {
        BlueSnackBar.make(binding.root.rootView, getString(R.string.over_selected)).show()
    }

    private fun navigateToFolder(folderName: String) {
        val action  = GalleryFragmentDirections.actionGalleryFragmentToFolderImageFragment(folderName)
        findNavController().navigate(action)
    }

    companion object {
        const val GALLERY_IMAGE_GRID_VIEW = 4
        const val GALLERY_FOLDER_GRID_VIEW = 2
    }
}