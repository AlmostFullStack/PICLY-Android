package com.easyhz.picly.data.repository.user

import com.easyhz.picly.domain.repository.user.UserRepository
import com.easyhz.picly.util.unknownErrorCode
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(
    private val fireStore: FirebaseFirestore,
) : UserRepository {

    override fun getCurrentUser(): FirebaseUser? = UserManager.currentUser

    override suspend fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String?) -> Unit) {
        try {
            val result = UserManager.login(email, password)
            onSuccess()
        } catch (e: FirebaseAuthException) {
            onError(e.errorCode)
        } catch (e: Exception) {
            onError(e.unknownErrorCode())
        }
    }

    override fun logout() {
        UserManager.logout()
    }
}