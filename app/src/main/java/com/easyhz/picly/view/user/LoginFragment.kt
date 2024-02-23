package com.easyhz.picly.view.user

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentLoginBinding
import com.easyhz.picly.view.navigation.NavControllerManager

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        setUp()
        onClick()
        return binding.root
    }

    private fun setUp() {
        setToolbar()
        setLoginWarning()
    }
    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.login_title)
    }

    private fun onClick() {
        binding.emailTextView.setOnClickListener {
            NavControllerManager.navigateLoginToLoginEmail()
        }
    }

    private fun setLoginWarning() {
        val loginWarningText = getString(R.string.login_warning)
        val spannableString = SpannableString(loginWarningText)

        WarningType.entries.forEach {
            viewModel.addClickableLink(requireContext(), spannableString, loginWarningText, getString(it.text)) { it.onClick() }
        }

        binding.loginWarningTextView.text = spannableString
        binding.loginWarningTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    enum class WarningType(val text: Int) {
        TERMS_OF_SERVICE(text = R.string.terms_of_service) {
            override fun onClick() {
                println("이용약관")
            }
        },
        PRIVACY_POLICY(text = R.string.privacy_policy) {
            override fun onClick() {
                println("개인정보처리방침")
            }
        };

        abstract fun onClick()
    }



}