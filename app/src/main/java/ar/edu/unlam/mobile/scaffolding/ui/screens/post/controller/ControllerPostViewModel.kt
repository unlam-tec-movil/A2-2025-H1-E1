package ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControllerPostViewModel
    @Inject
    constructor(
        private val postRepository: PostRespository,
    ) : ViewModel() {
        // TODO: Pantalla para crear una nueva publicación.
        private val _newPost = MutableStateFlow<NewPostUiState>(NewPostUiState.Init)
        val newPost: StateFlow<NewPostUiState> get() = _newPost

        fun newPost(
            message: String,
            userToken: String,
        ) {
            if (message.isBlank()) {
                _newPost.value = NewPostUiState.Error("El texto no puede estar vacío")
                return
            }

            viewModelScope.launch {
                _newPost.value = NewPostUiState.Loading

                try {
                    val result = postRepository.postTuit(message, userToken)
                    if (result.isSuccess) {
                        _newPost.value = NewPostUiState.Success
                    } else {
                        _newPost.value = NewPostUiState.Error("Error en la solicitud")
                    }
                } catch (e: Exception) {
                    _newPost.value = NewPostUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }

sealed interface NewPostUiState {
    object Init : NewPostUiState

    object Loading : NewPostUiState

    object Success : NewPostUiState

    data class Error(
        val message: String,
    ) : NewPostUiState
}
