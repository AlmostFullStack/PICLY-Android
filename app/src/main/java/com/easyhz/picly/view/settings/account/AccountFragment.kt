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
import com.easyhz.picly.view.dialog.EitherDialog
import com.easyhz.picly.view.dialog.Orientation
import com.easyhz.picly.view.navigation.NavControllerManager

class AccountFragment: Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]
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
                viewModel.logout {
                    NavControllerManager.navigateAccountToLogin()
                }
            }.setNegativeButton(getString(R.string.cancel), ContextCompat.getColor(requireActivity(), R.color.highlightBlue)) {

            }.show(requireActivity().supportFragmentManager)
        }
    }

    private fun onClickDeleteUser() {
        binding.deleteUserButton.setOnClickListener {
            val dialog = EitherDialog.instance(
                title = getString(R.string.dialog_delete_user_title),
                message = getString(R.string.dialog_delete_user_message),
                Orientation.HORIZONTAL
            )
            dialog.setPositiveButton(getString(R.string.recertification), ContextCompat.getColor(requireActivity(), R.color.errorColor)) {
                viewModel.deleteUser {
                    NavControllerManager.navigateAccountToLogin()
                }
            }.setNegativeButton(getString(R.string.cancel), ContextCompat.getColor(requireActivity(), R.color.highlightBlue)) {

            }.show(requireActivity().supportFragmentManager)
        }
    }
}