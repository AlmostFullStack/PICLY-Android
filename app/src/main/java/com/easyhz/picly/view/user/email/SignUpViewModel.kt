package com.easyhz.picly.view.user.email

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.model.result.UserResult
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.usecase.album.upload.UploadUseCase
import com.easyhz.picly.domain.usecase.user.SignUpUseCase
import com.easyhz.picly.util.getDefaultImage
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val uploadUseCase: UploadUseCase
): ViewModel() {

    suspend fun signUp(
        context: Context,
        email: String,
        password: String,
        authProvider: String,
        uid: String = ""
    ): UserResult {
        isEmptyEmailAndPassword(email, password)?.let {
            return UserResult.Error(it)
        }
        val signUpResult = signUpUseCase.signUp(UserForm(email, password, authProvider, uid))
        if (signUpResult is UserResult.Error) {
            return UserResult.Error(signUpResult.errorMessage)
        }

        val initAlbumResult = initAlbum(context)
        if (initAlbumResult is AlbumResult.Error) {
            return UserResult.Error(initAlbumResult.errorMessage)
        }
        return UserResult.Success
    }

    private suspend fun initAlbum(context: Context): AlbumResult<String> {
        UserManager.currentUser?.uid?.let { id ->
            val defaultGalleryItem = getDefaultGalleryItem(context)
            return uploadUseCase.writeAlbums(
                ownerId = id,
                tags = TAGS,
                selectedImageList = listOf(defaultGalleryItem!!),
                expireTime = getExpireTime()
            )
        } ?: return AlbumResult.Error("알 수 없는 오류가 발생했습니다")
    }

    private fun getDefaultGalleryItem(context: Context): GalleryImageItem? {
        return getDefaultImage(context)?.run {
            GalleryImageItem(
                id = length(),
                path = path,
                uri = toUri(),
                name = name,
                regDate = "",
                size = 100,
                width = 938,
                height = 938,
                isSelected = false,
                position = 0
            )
        }
    }

    private fun getExpireTime(): Timestamp {
        val currentTime = Timestamp.now().toDate()
        val calendar = Calendar.getInstance().apply {
            time = currentTime
            add(Calendar.DAY_OF_MONTH, 30) // 30일 추가
        }
        return Timestamp(calendar.time)
    }


    private fun isEmptyEmailAndPassword(email: String, password: String): String? = when {
        email.isEmpty() -> AuthError.ERROR_EMAIL_EMPTY.name
        password.isEmpty() -> AuthError.ERROR_PASSWORD_EMPTY.name
        else -> null
    }

    companion object {
        val TAGS = listOf("PICLY", "새로운", "공유의", "시작")
    }

}