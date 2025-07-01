package ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.DraftRepository
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.UserRepository
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
        private val draftRepository: DraftRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        // TODO: Pantalla para crear una nueva publicación.
        private val _newPost = MutableStateFlow<NewPostUiState>(NewPostUiState.Init)
        val newPost: StateFlow<NewPostUiState> get() = _newPost

        private val _draftSaved = MutableStateFlow(false)
        val draftSaved: StateFlow<Boolean> = _draftSaved

        // TODO: Obtener email del usuario desde DataStore
        init {
            viewModelScope.launch {
                userRepository.insertUser(UserEntity("alexis", "a", "a"))
            }
        }

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
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handlePostError(e)
                    _newPost.value = NewPostUiState.Error(errorMsg)
                }
            }
        }

        fun saveDraft(text: String?) {
            viewModelScope.launch {
                if (!text.isNullOrBlank()) {
                    // TODO: Obtener email del usuario desde DataStore
                    draftRepository.insertDraft(DraftEntity(text = text, userEmail = "alexis"))
                    _draftSaved.value = true
                    // Resetear el estado después de 3 segundos
                    kotlinx.coroutines.delay(3000)
                    _draftSaved.value = false
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
