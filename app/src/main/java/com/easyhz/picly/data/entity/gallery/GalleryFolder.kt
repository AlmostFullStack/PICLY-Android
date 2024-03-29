package com.easyhz.picly.data.entity.gallery

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryFolderItem
import com.easyhz.picly.util.getLongColumnOrThrow
import com.easyhz.picly.util.getStringColumnOrThrow

data class GalleryFolder(
    val id: String,
    val folderName: String,
    val thumbnailUri: Uri,
    val count: Int
) {
    companion object {
        fun createFromCursor(cursor: Cursor, uri: Uri): GalleryFolder {
            val id = cursor.getLongColumnOrThrow(MediaStore.Images.ImageColumns._ID) ?: -1
            val folderName = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
            val bucketId = cursor.getStringColumnOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
            val contentUri = ContentUris.withAppendedId(uri, id)

            return GalleryFolder(
                id = bucketId ?: "",
                folderName = folderName ?: "",
                thumbnailUri = contentUri,
                count = 0
            )
        }

        fun GalleryFolder.toGalleryFolderItem() : GalleryFolderItem =
            GalleryFolderItem(
                id = id,
                folderName = folderName,
                thumbnailUri = thumbnailUri,
                count = count
            )
    }
}