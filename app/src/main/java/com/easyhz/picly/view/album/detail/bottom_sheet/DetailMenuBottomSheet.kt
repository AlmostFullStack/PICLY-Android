package com.easyhz.picly.view.album.detail.bottom_sheet

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.R
import com.easyhz.picly.databinding.BottomSheetDetailMenuBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.dialog.EitherDialog
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.dialog.Orientation
import com.easyhz.picly.view.navigation.NavControllerManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailMenuBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDetailMenuBinding
    private lateinit var documentId: String
    private lateinit var viewModel: DetailMenuBottomSheetViewModel
    private lateinit var loading: LoadingDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDetailMenuBinding.inflate(layoutInflater)
        documentId = arguments?.getString("documentId").toString()
        viewModel = ViewModelProvider(requireActivity())[DetailMenuBottomSheetViewModel::class.java]
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.detailMenuBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        onClickShare()
        onClickDelete()
        onClickCancel()
    }

    private fun onClickShare() {
        binding.shareButton.setOnClickListener {
            dismiss()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, documentId.toPICLY())
            }
            startActivity(Intent.createChooser(intent, documentId.toPICLY()))
        }
    }

    private fun onClickDelete() {
        binding.deleteButton.setOnClickListener {
            dismiss()
            val dialog = EitherDialog.instance(
                title = getString(R.string.dialog_delete_title),
                message = getString(R.string.dialog_delete_message),
                Orientation.HORIZONTAL
            )
            dialog.setPositiveButton(getString(R.string.delete)) {
                loading.show(true)
                viewModel.deleteAlbum(documentId, onSuccess = { onSuccess() }) {
                    onFailure(it)
                }
            }.setNegativeButton(getString(R.string.cancel)) {

            }.show(requireActivity().supportFragmentManager)
        }
    }

    private fun onSuccess() {
        NavControllerManager.navigateDetailBottomMenuToMain()
        loading.show(false)
    }

    private fun onFailure(message: String) {
        BlueSnackBar.make(binding.root, message).show()
        loading.show(false)
    }
    private fun onClickCancel() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

}