package com.easyhz.picly.domain.usecase.album

import android.content.Context
import android.graphics.Bitmap
import com.easyhz.picly.util.image.ImageDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadImageUseCase {
    suspend fun download(context: Context, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        return@withContext ImageDownloader(context).download(bitmap)
    }
}