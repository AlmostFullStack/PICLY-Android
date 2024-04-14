package com.easyhz.picly.view.album.detail.image

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.easyhz.picly.R
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.usecase.album.DownloadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailImageViewModel
@Inject constructor(
    private val downloadImageUseCase: DownloadImageUseCase
): ViewModel() {

    suspend fun download(context: Context, bitmap: Bitmap): AlbumResult<String> = withContext(Dispatchers.IO) {
        try {
            val result = downloadImageUseCase.download(context, bitmap)
            if(result) {
                return@withContext AlbumResult.Success(context.getString(R.string.save_to_image_success))
            } else {
                return@withContext AlbumResult.Error(context.getString(R.string.save_to_image_failure))
            }
        } catch (e: Exception) {
            return@withContext AlbumResult.Error(context.getString(R.string.save_to_image_failure))
        }
    }
}