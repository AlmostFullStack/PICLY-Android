package com.easyhz.picly.data.repository.album

import com.easyhz.picly.data.entity.album.Album
import com.easyhz.picly.data.firebase.Constants.ALBUMS
import com.easyhz.picly.data.firebase.Constants.CREATION_TIME
import com.easyhz.picly.data.firebase.Constants.OWNER_ID
import com.easyhz.picly.domain.repository.album.AlbumRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AlbumRepositoryImpl
@Inject constructor(
    private val fireStore: FirebaseFirestore,
): AlbumRepository {
    override fun fetchAlbums() : Flow<List<Album>> = flow {
        try {
            // TODO: OWNER_ID 관리
            val result = fireStore.collection(ALBUMS)
                .whereEqualTo(OWNER_ID, "19bIo1GzbzPmZsjoHEeOTKf8WAm1")
                .orderBy(CREATION_TIME, Query.Direction.DESCENDING)
                .get()
                .await()

            val albums = result.toObjects<Album>()
            emit(albums)
        } catch (e: Exception) {
            // 예외 처리
            println("Error fetching albums: ${e.message}")
        }
    }

}