package com.easyhz.picly.data.repository.user

import android.util.Log
import com.easyhz.picly.data.entity.user.UserInfo
import com.easyhz.picly.data.firebase.Constants.AUTH_PROVIDER_EMAIL
import com.easyhz.picly.data.firebase.Constants.USERS
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.util.unknownErrorCode
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(
    private val fireStore: FirebaseFirestore,
) : UserRepository {

    override fun getCurrentUser(): FirebaseUser? = UserManager.currentUser

    override suspend fun login(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit) {
        try {
            val result = UserManager.login(user.email, user.password)
            onSuccess.invoke()
        } catch (e: FirebaseAuthException) {
            onError(e.errorCode)
        } catch (e: Exception) {
            onError(e.unknownErrorCode())
        }
    }

    override fun logout() {
        UserManager.logout()
    }

    override suspend fun signUp(user: UserForm, onSuccess: () -> Unit, onError: (String?) -> Unit) {
        try {
            val result = UserManager.signUp(user.email, user.password).user
            val email = result?.email ?: throw IllegalArgumentException("Email should not be null.")

            saveUser(result.uid, email)
            onSuccess.invoke()
        } catch (e: FirebaseAuthException) {
            onError(e.errorCode)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Sign Up Error: ${e.message}")
            onError(e.unknownErrorCode())
        }
    }

    private suspend fun saveUser(documentId: String, email: String) {
        val user = UserInfo(
            authProvider = AUTH_PROVIDER_EMAIL,
            creationTime = Timestamp.now(),
            email = email
        )
        try {
            fireStore
                .collection(USERS)
                .document(documentId)
                .set(user)
                .await()
        } catch (e: Exception) {
            throw Exception("Error saving user to Firestore", e)
        }
    }
}