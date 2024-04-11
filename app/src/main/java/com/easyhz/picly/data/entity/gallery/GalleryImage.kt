package com.easyhz.picly.data.entity.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.easyhz.picly.data.entity.album.ImageSize
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.util.getImageDimensions
import com.easyhz.picly.util.getLongColumnOrThrow
import com.easyhz.picly.util.getStringColumnOrThrow

data class GalleryImage(
    val id: Long,
    val path: String,
    val uri: Uri,
    val name: String,
    val regDate: String,
    val size: Long,
    val width: Long,
    val height: Long,
) {
    companion object {
        fun createFromCursor(cursor: Cursor, uri: Uri, type: Int = 1): GalleryImage {
            val id = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns._ID) ?: -1
            val name = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val path = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATA)
            val regDate = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
            val size = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.SIZE) ?: 100
            val width = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.WIDTH) ?: 938
            val height = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.HEIGHT) ?: 938
            val contentUri = ContentUris.withAppendedId(uri, id)

            return GalleryImage(
                id = id,
                path = path ?: "",
                uri = if (type == 1) contentUri else uri,
                name = name ?: "",
                regDate = regDate ?: "" ,
                size = size,
                width = width,
                height = height
            )
        }

        suspend fun createFromCursor(cursor: Cursor, uri: Uri, context: Context): GalleryImage {
            val id = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns._ID) ?: -1
            val name = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
            val path = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATA)
            val regDate = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
            val size = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.SIZE) ?: 100
            val width = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.WIDTH)
            val height = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns.HEIGHT)
            val imageSize = if (width == null || height == null) {
                context.getImageDimensions(uri)
            } else {
                ImageSize(height = height, width = width)
            }

            return GalleryImage(
                id = id,
                path = path ?: "",
                uri = uri,
                name = name ?: "",
                regDate = regDate ?: "" ,
                size = size,
                width = imageSize.width,
                height = imageSize.height
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
                width = width,
                height = height,
                isSelected = false,
                position = position
            )

        fun GalleryImage.toGalleryImageItem() : GalleryImageItem =
            GalleryImageItem(
                id = id,
                path = path,
                uri = uri,
                name = name,
                regDate = regDate,
                size = size,
                width = width,
                height = height
            )
    }
}
