package com.easyhz.picly.view.user

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.easyhz.picly.R
import com.easyhz.picly.data.firebase.Constants.AUTH_PROVIDER_GOOGLE
import com.easyhz.picly.data.repository.user.UserManager.initGoogle
import com.easyhz.picly.data.repository.user.UserManager.onGoogleSignInAccount
import com.easyhz.picly.databinding.FragmentLoginBinding
import com.easyhz.picly.helper.PRIVACY_POLICY_URL
import com.easyhz.picly.helper.TERMS_OF_SERVICE_URL
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import com.easyhz.picly.view.user.email.SignUpViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var signUpViewModel: SignUpViewModel
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var loading: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        googleSignInClient = initGoogle(requireActivity())
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        setToolbar()
        setLoginWarning()
        onClickEmailLogin()
        onClickGoogleLogin()
        callBack()
    }
    private fun setToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.login_title)
    }

    private fun onClickEmailLogin() {
        binding.emailTextView.setOnClickListener {
            NavControllerManager.navigateLoginToLoginEmail()
        }
    }

    private fun setLoginWarning() {
        val loginWarningText = getString(R.string.login_warning)
        val spannableString = SpannableString(loginWarningText)

        WarningType.entries.forEach {
            viewModel.addClickableLink(requireContext(), spannableString, loginWarningText, getString(it.text)) {
                val intent = Intent(ACTION_VIEW).apply {
                    data = Uri.parse(it.url)
                }
                startActivity(intent)
            }
        }

        binding.loginWarningTextView.text = spannableString
        binding.loginWarningTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun onClickGoogleLogin() {
        binding.googleButton.setOnClickListener {
            signIntWithGoogle()
        }
    }
    private fun signIntWithGoogle() {
        googleSignInClient?.signInIntent?.run {
            getResult.launch(this)
        }
    }
    private fun callBack() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            loading.show(true)
            Log.d("LoginFragment", "result $result -> ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                val signInTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = signInTask.getResult(ApiException::class.java)
                    onGoogleSignInAccount(account) { onFirebaseAuthTask(it) }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            } else {
                onFailure("login")
                return@registerForActivityResult
            }
        }
    }

    private fun onFirebaseAuthTask(task: Task<AuthResult>) {
        if (!task.isSuccessful) {
            onFailure("${task.exception?.localizedMessage}")
            return
        }
        if (task.result?.additionalUserInfo?.isNewUser == true) {
            val user = task.result.user
            user?.let { u ->
                u.email?.let {
                    signUpViewModel.signUp(
                        requireActivity(),
                        email = it,
                        password = u.uid,
                        uid = u.uid,
                        authProvider = AUTH_PROVIDER_GOOGLE,
                        onSuccess = { onSuccess() }
                    ) {
                        onFailure(it)
                    }
                }
            }
        } else {
            onSuccess()
        }
    }

    private fun onSuccess() {
        NavControllerManager.navigateLoginToMain()
        loading.show(false)
    }

    private fun onFailure(e: String) {
        BlueSnackBar.make(binding.root, getString(R.string.error_unknown)).show()
        loading.show(false)
    }


    enum class WarningType(val text: Int, val url: String) {
        TERMS_OF_SERVICE(text = R.string.terms_of_service, url = TERMS_OF_SERVICE_URL),
        PRIVACY_POLICY(text = R.string.privacy_policy, url = PRIVACY_POLICY_URL)
    }



}