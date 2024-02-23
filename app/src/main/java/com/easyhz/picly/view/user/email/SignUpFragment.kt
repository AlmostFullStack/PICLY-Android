package com.easyhz.picly.view.user.email

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.R
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.databinding.FragmentEmailLoginBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.user.setEmailField
import com.easyhz.picly.util.user.setPasswordField
import com.easyhz.picly.view.navigation.NavControllerManager

class SignUpFragment :Fragment() {
    private lateinit var binding: FragmentEmailLoginBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        initOnErrorEvent()
        viewModel.onErrorEvent.removeObservers(viewLifecycleOwner)
    }

    private fun setUp() {
        setToolbar()
        setEmailField(requireContext(), binding.userField)
        setPasswordField(requireContext(), binding.userField)
        observeErrorEvent()
        setButton()
    }

    private fun initOnErrorEvent() {
        viewModel.setOnErrorEvent()
    }

    private fun observeSuccessEvent() {
        viewModel.onSuccessEvent.observe(viewLifecycleOwner) {

        }
    }

    private fun observeErrorEvent() {
        viewModel.onErrorEvent.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                BlueSnackBar.make(binding.root, getString(AuthError.valueOf(it).messageId)).show()
            }
        }
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            toolbarTitle.text = getString(R.string.sign_up)
            toolbarTitle.gravity = Gravity.CENTER
            toolbarTitle.textSize = 20F
            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener {
                NavControllerManager.navigateToBack()
            }
        }
    }

    private fun setButton() {
        binding.loginButton.button.apply {
            text = getString(R.string.sign_up)
            setOnClickListener {
                viewModel.signUp(
                    binding.userField.emailField.editText.text.toString(),
                    binding.userField.passwordField.editText.text.toString()
                )
            }
        }
    }

}