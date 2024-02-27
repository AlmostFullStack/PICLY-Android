package com.easyhz.picly.data.entity.gallery

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.easyhz.picly.domain.model.album.upload.GalleryImageItem
import com.easyhz.picly.util.getLongColumnOrThrow
import com.easyhz.picly.util.getStringColumnOrThrow

data class GalleryImage(
    val id: Long,
    val path: String,
    val uri: Uri,
    val name: String,
    val regDate: String,
    val size: Int
) {
    companion object {
        fun createFromCursor(cursor: Cursor, uri: Uri): GalleryImage {
            val id = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns._ID)
            val name = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val path = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATA)
            val regDate = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
            val contentUri = ContentUris.withAppendedId(uri, id)

            return GalleryImage(
                id = id,
                path = path ?: "",
                uri = contentUri,
                name = name ?: "",
                regDate = regDate ?: "" ,
                size = 0
            )
        }

        fun GalleryImage.toGalleryImageItem(position: Int) : GalleryImageItem =
            GalleryImageItem(
                id = id,
                path = path,
                uri = uri,
                name = name,
                regDate = regDate,
                size = size,
                isSelected = false,
                position = position
            )
    }
}
