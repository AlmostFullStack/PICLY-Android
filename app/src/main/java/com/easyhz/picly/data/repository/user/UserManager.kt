package com.easyhz.picly.data.repository.user

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
}