package com.easyhz.picly.data.repository.gallery

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.easyhz.picly.data.entity.gallery.GalleryFolder
import com.easyhz.picly.data.entity.gallery.GalleryImage
import com.easyhz.picly.domain.repository.gallery.GalleryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * 1. [Content Provider](https://developer.android.com/guide/topics/providers/content-provider-basics?hl=ko)
 * 2. [Content Resolver](https://developer.android.com/privacy-and-security/risks/content-resolver?hl=ko)
 * 3. [Data Storage](https://developer.android.com/training/data-storage/shared/media?hl=ko)
 */
class GalleryRepositoryImpl
@Inject constructor(
    @ApplicationContext private val context: Context
): GalleryRepository {
    private val uri: Uri by lazy {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private val contentResolver by lazy {
        context.contentResolver
    }

    private val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.Images.ImageColumns.ALBUM,
        MediaStore.Images.ImageColumns.SIZE,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
    )

    private val orderByThis = MediaStore.Images.ImageColumns.DATE_TAKEN

    override fun fetchImages(
        page: Int,
        loadSize: Int,
        currentLocation: String?,
    ): MutableList<GalleryImage> {
        val imageList = mutableListOf<GalleryImage>()
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (currentLocation != null) {
            selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ? "
            selectionArgs = arrayOf("$currentLocation")
        }

        val offset = (page - 1) * loadSize
        val query = getQuery(offset, loadSize, selection, selectionArgs)
        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val image = GalleryImage.createFromCursor(cursor, uri)
                imageList.add(image)
            }
        }
        return imageList
    }

    override fun fetchFolder(page: Int, loadSize: Int): MutableList<GalleryFolder> {
        val folderList = mutableListOf<GalleryFolder>()
        val offset = (page - 1) * loadSize
        val query = getQuery(offset, loadSize, null, null)
        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val folder = GalleryFolder.createFromCursor(cursor, uri)
                if (folderList.none { it.id == folder.id }) {
                    val count = getCount(context, uri, folder.id)
                    if (count == 0) continue
                    folderList.add(folder.copy(count = count))
                }
            }
        }
        return folderList
    }

    private fun getQuery(
        offset: Int,
        limit: Int,
        selection : String?,
        selectionArgs: Array<String>?
    ) = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        val bundle = bundleOf(
            ContentResolver.QUERY_ARG_OFFSET to offset,
            ContentResolver.QUERY_ARG_LIMIT to limit,
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_TAKEN),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
            ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
            ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs,
        )
        contentResolver.query(uri, projection, bundle, null)
    } else {
        contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "$orderByThis DESC LIMIT $limit OFFSET $offset"
        )
    }

    private fun getCount(context: Context, contentUri: Uri, bucketId: String): Int {
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucketId)

        return context.contentResolver.query(
            contentUri,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.count
            } else {
                0
            }
        } ?: 0
    }
}