package com.easyhz.picly.util.image

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.easyhz.picly.data.entity.album.ImageSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class ImageDownloader(private val context: Context) {
    suspend fun download(imageUrl: String): Boolean = withContext(Dispatchers.IO) {
        val bitmap = downloadImage(imageUrl)
        return@withContext saveImageToGallery(bitmap)
    }

    suspend fun download(bitmap: Bitmap): Boolean = withContext(Dispatchers.IO) {
        return@withContext saveImageToGallery(bitmap)
    }


    /**
     * 이미지 url에서 다운로드 하는 함수
     *
     * @return Bitmap
     */
    private suspend fun downloadImage(imageUrl: String): Bitmap = withContext(Dispatchers.IO) {
        val inputStream = URL(imageUrl).openStream()
        return@withContext BitmapFactory.decodeStream(inputStream)
    }

    /**
     * 갤러리에 저장하는 함수
     */
    private fun saveImageToGallery(bitmap: Bitmap): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageAboveQ(bitmap)
        } else {
            saveImageBelowQ(bitmap)
        }
    }
    private fun saveImageAboveQ(bitmap: Bitmap): Boolean {
        val contentResolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpeg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            return true
        } ?: run { return false }
    }

    private fun saveImageBelowQ(bitmap: Bitmap): Boolean {
        val imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val file = File(imagePath, "${System.currentTimeMillis()}.jpeg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return true
    }


    /**
     * 이미지 캐시 디렉토리에 저장하는 함수
     *
     */
    suspend fun saveImageToCache(uri: Uri, cacheFile: File) {
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    inputStream.copyTo(outputStream, bufferSize = 1024)
                }
            }
        }
    }

    /**
     *  이미지 가로 세로를 가져오는 함수
     *
     *  @return ImageSize
     */
    suspend fun getImageDimensions(uri: Uri): ImageSize = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            return@withContext ImageSize(height = options.outHeight.toLong(), width = options.outWidth.toLong())
        } ?: return@withContext ImageSize(938, 938)
    }
}
