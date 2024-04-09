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
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.showAlertDialog
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            showAlertDialog(
                context = requireContext(),
                title= R.string.dialog_delete_title,
                message = R.string.dialog_delete_message,
                positiveButtonText = R.string.delete,
                onContinue = {
                    onContinue()
                },
                negativeButtonText = R.string.cancel,
                onCancel = { },
                style = R.style.DialogDeleteTheme
            )
//            val dialog = EitherDialog.instance(
//                title = getString(R.string.dialog_delete_title),
//                message = getString(R.string.dialog_delete_message),
//                Orientation.HORIZONTAL
//            )
//            dialog.setPositiveButton(getString(R.string.delete)) {
//                loading.show(true)
//                viewModel.deleteAlbum(documentId, onSuccess = { onSuccess() }) {
//                    onFailure(it)
//                }
//            }.setNegativeButton(getString(R.string.cancel)) {
//
//            }.show(requireActivity().supportFragmentManager)
        }
    }

    private fun onContinue() {
        loading.show(true)
        CoroutineScope(Dispatchers.Main).launch {
            when(val result = viewModel.deleteAlbum(documentId)) {
                is DeleteAlbumUseCase.DeleteAlbumResult.Success -> onSuccess()
                is DeleteAlbumUseCase.DeleteAlbumResult.Error -> onFailure(result.errorMessage)
            }
            loading.show(false)
        }
    }
    private fun onSuccess() {
        NavControllerManager.navigateDetailBottomMenuToMain()
    }

    private fun onFailure(message: String) {
        BlueSnackBar.make(binding.root, message).show()
    }
    private fun onClickCancel() {
//        binding.cancelButton.setOnClickListener {
//            dismiss()
//        }
    }

}