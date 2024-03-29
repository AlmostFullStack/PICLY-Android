package com.easyhz.picly.view.album.detail.bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyhz.picly.domain.usecase.album.DeleteAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMenuBottomSheetViewModel
@Inject constructor(
    private val deleteAlbumUseCase: DeleteAlbumUseCase
): ViewModel() {

    fun deleteAlbum(
        id: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit,
    ) = viewModelScope.launch {
        deleteAlbumUseCase(id)
            .catch { e->
                e.localizedMessage?.let { onFailure(it) }
            }.collectLatest {
                onSuccess(it)
            }
    }
}