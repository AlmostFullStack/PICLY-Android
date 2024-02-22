package com.easyhz.picly.view.user

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentEmailLoginBinding
import com.easyhz.picly.view.navigation.NavControllerManager
import com.google.android.material.textfield.TextInputLayout

class EmailLoginFragment :Fragment() {
    private lateinit var binding: FragmentEmailLoginBinding
    private val args: EmailLoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setPasswordField()
        setButton()
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            toolbarTitle.text = getString(args.type)
            toolbarTitle.gravity = Gravity.CENTER
            toolbarTitle.textSize = 20F
            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener {
                NavControllerManager.navigateToBack()
            }

            if (shouldShowSignUpButton(args.type)) {
                signUpTextView.visibility = View.VISIBLE
                signUpTextView.setOnClickListener {
                    navigateToSignUp()
                }
            }
        }
    }

    private fun setPasswordField() {
        binding.userField.passwordField.apply {
            textViewLabel.text = getString(R.string.password_text)
            editText.hint = getString(R.string.password_hint)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editTextField.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }
    }

    private fun setButton() {
        binding.loginButton.button.text = getString(args.type)
    }

    private fun shouldShowSignUpButton(type: Int): Boolean = type == R.string.login_title

    private fun navigateToSignUp() {
        val action = EmailLoginFragmentDirections.actionLoginFragmentToEmailLoginFragment(R.string.sign_up)
        findNavController().navigate(action)
    }

}