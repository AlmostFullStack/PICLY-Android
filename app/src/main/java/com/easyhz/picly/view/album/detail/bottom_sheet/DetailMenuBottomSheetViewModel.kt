package com.easyhz.picly.view.album.detail.bottom_sheet

import androidx.lifecycle.ViewModel
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailMenuBottomSheetViewModel
@Inject constructor(
    private val deleteAlbumUseCase: DeleteAlbumUseCase
): ViewModel() {

    suspend fun deleteAlbum(id: String): AlbumResult<String> = deleteAlbumUseCase(id)

}