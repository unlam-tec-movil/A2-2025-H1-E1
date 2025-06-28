package ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel
    @Inject
    constructor(
        private val postRepository: PostRepository,
    ) : ViewModel() {
        private val _replies = MutableStateFlow<RepliesUiState>(RepliesUiState.Loading)
        val replies: StateFlow<RepliesUiState> get() = _replies

        private val _replySent = MutableStateFlow<ReplySentUiState>(ReplySentUiState.Init)
        val replySent: StateFlow<ReplySentUiState> get() = _replySent

        fun getReplies(
            idTuit: Int,
            userToken: String,
        ) {
            viewModelScope.launch {
                try {
                    val result = postRepository.getReplies(idTuit, userToken)
                    _replies.value = RepliesUiState.Success(result)
                } catch (e: Exception) {
                    _replies.value = RepliesUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        fun replyToTuit(
            idTuit: Int,
            message: String,
            userToken: String,
        ) {
            if (message.isBlank()) {
                _replySent.value = ReplySentUiState.Error("El texto no puede estar vacío")
                return
            }

            viewModelScope.launch {
                _replySent.value = ReplySentUiState.Loading

                try {
                    postRepository.replyToTuit(idTuit, message, userToken)
                    _replySent.value = ReplySentUiState.Success
                } catch (e: Exception) {
                    _replySent.value = ReplySentUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }

sealed interface RepliesUiState {
    object Loading : RepliesUiState

    data class Success(val replies: List<ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit>) : RepliesUiState

    data class Error(val message: String) : RepliesUiState
}

sealed interface ReplySentUiState {
    object Init : ReplySentUiState

    object Loading : ReplySentUiState

    object Success : ReplySentUiState

    data class Error(val message: String) : ReplySentUiState
}

