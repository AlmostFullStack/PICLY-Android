package com.easyhz.picly.view.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentAccountBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.view.dialog.EitherDialog
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.dialog.Orientation
import com.easyhz.picly.view.navigation.NavControllerManager

class AccountFragment: Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        fetchUserInfo()
        observeUserInfo()
        onClickLogout()
        onClickDeleteUser()
        onClickBackButton()
    }

    private fun fetchUserInfo() {
        viewModel.fetchUserInfo()
    }

    private fun observeUserInfo() {
        viewModel.userInfo.observe(viewLifecycleOwner) {
            binding.data = it
        }
    }

    private fun onClickLogout() {
        binding.logoutButton.setOnClickListener {
            val dialog = EitherDialog.instance(
                title = getString(R.string.dialog_logout_title),
                message = getString(R.string.dialog_logout_message),
                Orientation.HORIZONTAL
            )
            dialog.setPositiveButton(getString(R.string.account_logout), ContextCompat.getColor(requireActivity(), R.color.errorColor)) {
                loading.show(true)
                viewModel.logout {
                    NavControllerManager.navigateAccountToLogin()
                    loading.show(false)
                }
            }.setNegativeButton(getString(R.string.cancel), ContextCompat.getColor(requireActivity(), R.color.highlightBlue)) {

            }.show(requireActivity().supportFragmentManager)
        }
    }

    private fun onClickDeleteUser() {
        binding.deleteUserButton.setOnClickListener {
            BlueSnackBar.make(binding.root, getString(R.string.ready)).show()
//            val dialog = EitherDialog.instance(
//                title = getString(R.string.dialog_delete_user_title),
//                message = getString(R.string.dialog_delete_user_message),
//                Orientation.HORIZONTAL
//            )
//            dialog.setPositiveButton(getString(R.string.recertification), ContextCompat.getColor(requireActivity(), R.color.errorColor)) {
//                viewModel.deleteUser {
//                    NavControllerManager.navigateAccountToLogin()
//                }
//            }.setNegativeButton(getString(R.string.cancel), ContextCompat.getColor(requireActivity(), R.color.highlightBlue)) {
//
//            }.show(requireActivity().supportFragmentManager)
        }
    }

    private fun onClickBackButton() {
        binding.toolbar.backButton.setOnClickListener {
            NavControllerManager.navigateToBack()
        }
    }
}