package ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.DraftRepository
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.UserRepository
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControllerPostViewModel
    @Inject
    constructor(
        private val postRepository: PostRepository,
        private val draftRespository: DraftRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _newPost = MutableStateFlow<NewPostUiState>(NewPostUiState.Init)
        val newPost: StateFlow<NewPostUiState> get() = _newPost

        private val _draftSaved = MutableStateFlow(false)
        val draftSaved: StateFlow<Boolean> = _draftSaved

        init {
            viewModelScope.launch {
                // Inserta un usuario de prueba en la base local
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
                    _newPost.value = NewPostUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        fun draftTuit(text: String?) {
            viewModelScope.launch {
                if (!text.isNullOrBlank()) {
                    // TODO: Obtener el email dinámicamente desde DataStore
                    draftRespository.insertDraft(DraftEntity(text = text, userEmail = "alexis"))
                    _draftSaved.value = true
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
