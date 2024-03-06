package com.easyhz.picly.data.repository.album

import android.net.Uri
import android.util.Log
import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.data.entity.album.ImageUrl
import com.easyhz.picly.data.firebase.Constants.ALBUMS
import com.easyhz.picly.data.firebase.Constants.CREATION_TIME
import com.easyhz.picly.data.firebase.Constants.OWNER_ID
import com.easyhz.picly.data.repository.user.UserManager
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.easyhz.picly.util.toPICLY
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AlbumRepositoryImpl
@Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
): AlbumRepository {
    override fun fetchAlbums() : Flow<List<Album>> = flow {
        try {
            val result = fireStore.collection(ALBUMS)
                .whereEqualTo(OWNER_ID, UserManager.currentUser?.uid)
                .orderBy(CREATION_TIME, Query.Direction.DESCENDING)
                .get()
                .await()

            val albums = result.toObjects<Album>()
            emit(albums)
        } catch (e: Exception) {
            // 예외 처리
            Log.e(this.javaClass.simpleName, "Error fetching albums: ${e.message}")
        }
    }
    override fun writeAlbums(album: Album): Flow<DocumentReference> = flow {
        try {
            val result = fireStore.collection(ALBUMS)
                .add(album)
                .await()
            emit(result)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error writing albums: ${e.message}")
            throw e
        }
    }

    override fun updateAlbums(id: String, album: Album): Flow<String> = flow {
        try {
            val result = fireStore
                .collection(ALBUMS)
                .document(id)
                .set(album)
                .await()
            emit(id.toPICLY())
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, "Error updating albums: ${e.message}")
            throw e
        }
    }

    override fun writeAlbumImages(id: String, imageUris: List<Uri>): Flow<ImageUrl> = flow {
        try {
            val thumbnailUrl = uploadImage(id, THUMBNAIL, imageUris[0])
            val imageUrls = imageUris.mapIndexed { index, uri ->
                val imageName = "$index.jpeg"
                uploadImage(id, imageName, uri)
            }
            val imageUrl = ImageUrl(thumbnailUrl, imageUrls)
            emit(imageUrl)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName,"Error writing album Images: ${e.message}")
            throw e
        }
    }

    override fun deleteAlbum(id: String): Flow<String> = flow {
        try {
            val documentResult = fireStore.collection(ALBUMS)
                .document(id)
                .delete()
                .await()
            val fileList = storage.reference.child(id).listAll().await()
            fileList.items.forEach { file ->
                file.delete().await()
            }
            emit(id)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName,"Error deleting album: ${e.message}")
            throw e
        }
    }

    private suspend fun uploadImage(id: String, imageName: String, uri: Uri): String {
        val imageRef = storage.reference.child(id).child(imageName)
        val updateFile = imageRef.putFile(uri).await()
        val result = updateFile.metadata?.reference?.downloadUrl?.await()
        return result.toString()
    }


    companion object {
        const val THUMBNAIL = "thumbnail.jpeg"
    }
}