package com.easyhz.picly.view.album.upload.gallery

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.databinding.GalleryBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class GalleryBottomSheetFragment:  BottomSheetDialogFragment() {
    private lateinit var binding: GalleryBottomSheetBinding
    private lateinit var viewModel: GalleryBottomSheetViewModel

    companion object {
        private var instance: GalleryBottomSheetFragment? = null

        fun getInstance(): GalleryBottomSheetFragment {
            if (instance == null) {
                instance = GalleryBottomSheetFragment()
            }
            return instance!!
        }

        fun releaseInstance() {
            instance = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GalleryBottomSheetBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[GalleryBottomSheetViewModel::class.java]
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setIsSelectedAlbum(false)
    }
}