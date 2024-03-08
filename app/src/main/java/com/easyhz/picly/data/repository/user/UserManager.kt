package com.easyhz.picly.data.repository.user

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.easyhz.picly.BuildConfig
import kotlinx.coroutines.tasks.await

object UserManager {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    suspend fun login(email: String, password: String): AuthResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun signUp(email: String, password: String): AuthResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

    suspend fun delete() {
        firebaseAuth.currentUser?.delete()?.await()
    }

    fun initGoogle(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    fun onGoogleSignInAccount(account: GoogleSignInAccount?, onSuccess: (Task<AuthResult>) -> Unit) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                onSuccess(it)
            }
        }
    }
}