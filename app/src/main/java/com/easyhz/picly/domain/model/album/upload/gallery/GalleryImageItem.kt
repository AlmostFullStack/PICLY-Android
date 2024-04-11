package com.easyhz.picly.domain.model.album.upload.gallery

import android.content.Context
import android.net.Uri
import android.os.Build
import com.easyhz.picly.data.entity.gallery.GalleryImage.Companion.createFromCursor
import com.easyhz.picly.data.entity.gallery.GalleryImage.Companion.toGalleryImageItem

data class GalleryImageItem(
    val id: Long,
    val path: String,
    val uri: Uri,
    val name: String,
    val regDate: String,
    val size: Long,
    val width: Long,
    val height: Long,
    var isSelected: Boolean = false,
    var position: Int = -1
) {
    companion object {
        suspend fun Uri.toGalleryImageItem(context: Context): GalleryImageItem? {
            val contentResolver = context.contentResolver
            val cursor = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) { // Android 11 이상
                contentResolver.query(this, null, null, null)
            } else { // Android 11 미만
                contentResolver.query(this, null, null, null, null)
            }
            cursor?.use { it ->
                while (it.moveToNext()) {
                    return createFromCursor(it, this, context).toGalleryImageItem()
                }
            }
            return null
        }

    }
}
