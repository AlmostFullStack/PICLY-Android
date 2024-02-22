package com.easyhz.picly.view.user

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentLoginBinding
import com.easyhz.picly.view.navigation.NavControllerManager

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setUp()
        onClick()
        return binding.root
    }

    private fun setUp() {
        setNavController()
        setToolbar()
        setLoginWarning()
    }

    private fun setNavController() {
        NavControllerManager.init(findNavController())
    }
    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.login_title)
    }

    private fun onClick() {
        binding.emailTextView.setOnClickListener {
            val action = EmailLoginFragmentDirections.actionLoginFragmentToEmailLoginFragment(R.string.login_title)
            findNavController().navigate(action)
        }
    }

    private fun setLoginWarning() {
        val loginWarningText = getString(R.string.login_warning)
        val spannableString = SpannableString(loginWarningText)

        WarningType.entries.forEach {
            addClickableLink(spannableString, loginWarningText, getString(it.text)) { it.onClick() }
        }

        binding.loginWarningTextView.text = spannableString
        binding.loginWarningTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun addClickableLink(
        spannableString: SpannableString,
        loginWarningText: String,
        linkText: String,
        clickAction: () -> Unit
    ) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickAction.invoke()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(requireContext(), R.color.highlightBlue) // 링크 색상 설정
            }
        }

        val startIndex = loginWarningText.indexOf(linkText)
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + linkText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
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