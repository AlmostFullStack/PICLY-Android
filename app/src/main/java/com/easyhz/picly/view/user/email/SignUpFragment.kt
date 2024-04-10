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
import com.easyhz.picly.data.firebase.Constants.AUTH_PROVIDER_EMAIL
import com.easyhz.picly.databinding.FragmentEmailLoginBinding
import com.easyhz.picly.domain.model.result.UserResult
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.user.setEmailField
import com.easyhz.picly.util.user.setPasswordField
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment :Fragment() {
    private lateinit var binding: FragmentEmailLoginBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }
    private fun setUp() {
        setToolbar()
        setEmailField(requireContext(), binding.userField)
        setPasswordField(requireContext(), binding.userField)
        setButton()
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
                loading.show(true)
                CoroutineScope(Dispatchers.Main).launch {
                    val result = viewModel.signUp(
                        requireContext(),
                        email = binding.userField.emailField.editText.text.toString(),
                        password = binding.userField.passwordField.editText.text.toString(),
                        authProvider = AUTH_PROVIDER_EMAIL,
                    )
                    when(result) {
                        is UserResult.Success -> onSuccess()
                        is UserResult.Error -> onFailure(result.errorMessage)
                    }
                }
            }
        }
    }

    private fun onSuccess() {
        NavControllerManager.navigateEmailSignUpToSignUp()
        loading.show(false)
    }

    private fun onFailure(message: String) {
        BlueSnackBar.make(binding.root, getString(AuthError.valueOf(message).messageId)).show()
        loading.show(false)
    }
}