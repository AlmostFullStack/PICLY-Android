package com.easyhz.picly.view.user.email

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.data.firebase.AuthError
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.model.user.UserForm
import com.easyhz.picly.domain.usecase.album.upload.UploadUseCase
import com.easyhz.picly.domain.usecase.user.SignUpUseCase
import com.easyhz.picly.util.getDefaultImage
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val uploadUseCase: UploadUseCase
): ViewModel() {

    fun signUp(context: Context, email: String, password: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) = viewModelScope.launch {
        isEmptyEmailAndPassword(email, password)?.let {
            onFailure(it)
            return@launch
        }
        signUpUseCase.signUp(
            UserForm(email, password),
            onSuccess = { initAlbum(context, onSuccess, onFailure) }
        ) {
            if (it != null) {
                onFailure(it)
            }
        }
    }

    private fun initAlbum(context: Context, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) = viewModelScope.launch {
        UserManager.currentUser?.uid?.let { id ->
            val defaultGalleryItem = getDefaultGalleryItem(context)
            uploadUseCase.writeAlbums(
                ownerId = id,
                tags = TAGS,
                selectedImageList = listOf(defaultGalleryItem!!),
                expireTime = getExpireTime()
            ).catch { e ->
                e.localizedMessage?.let { onFailure(it) }
            }.collectLatest {
                onSuccess(it)
            }
        }
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
                position = 1
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