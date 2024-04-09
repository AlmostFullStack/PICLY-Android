package com.easyhz.picly.data.repository.user

import android.util.Log
import com.easyhz.picly.data.entity.user.UserInfo
import com.easyhz.picly.data.firebase.Constants.AUTH_PROVIDER_EMAIL
import com.easyhz.picly.data.firebase.Constants.USERS
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.repository.user.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(
    private val fireStore: FirebaseFirestore,
) : UserRepository {

    override fun getCurrentUser(): FirebaseUser? = UserManager.currentUser

    override suspend fun login(user: UserForm) {
        try {
            val result = UserManager.login(user.email, user.password)
        } catch (e: Exception) {
           throw e
        }
    }

    override fun fetchLoginInfo(id: String): Flow<UserInfo> = flow {
        try {
            val result = fireStore.collection(USERS)
                .document(id)
                .get()
                .await()
            val userInfo = result.toObject<UserInfo>()
            if (userInfo != null) {
                emit(userInfo)
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error fetching userInfo: ${e.message}")
        }
    }

    override fun logout() {
        UserManager.logout()
    }

    override suspend fun signUp(user: UserForm) {
        try {
            var uid = user.uid
            var email = user.email
            if(user.authProvider == AUTH_PROVIDER_EMAIL) {
                val result = UserManager.signUp(user.email, user.password).user
                email = result?.email ?: throw IllegalArgumentException("Email should not be null.")
                uid = result.uid
            }

            saveUser(uid, email, user.authProvider)
        } catch (e: FirebaseAuthException) {
            throw e
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Sign Up Error: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteUser(id: String, onSuccess: () -> Unit) {
        try {
            UserManager.delete()
            val result = fireStore.collection(USERS)
                .document(id)
                .delete()
                .await()

            onSuccess.invoke()
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "delete Error : ${e.message}")
        }
    }


    private suspend fun saveUser(documentId: String, email: String, authProvider: String) {
        val user = UserInfo(
            authProvider = authProvider,
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