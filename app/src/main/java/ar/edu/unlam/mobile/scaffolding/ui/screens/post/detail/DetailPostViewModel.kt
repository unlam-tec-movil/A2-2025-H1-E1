package ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPostViewModel
    @Inject
    constructor(
        private val postRespository: PostRespository,
    ) : ViewModel() {
        private val _comments = MutableStateFlow<CommentsState>(CommentsState.Loading)
        val comments: StateFlow<CommentsState> get() = _comments

        fun getComments(idTuit: Int) {
            viewModelScope.launch {
                try {
                    val result = postRespository.getReplies(idTuit)
                    if (result.isNotEmpty()) {
                        _comments.value = CommentsState.Success(result)
                    } else {
                        _comments.value = CommentsState.Error("No se encontraron comentarios")
                    }
                } catch (e: Exception) {
                    _comments.value = CommentsState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        fun sendComment(
            idTuit: Int,
            message: String,
        ) {
            viewModelScope.launch {
                try {
                    postRespository.replyToTuit(idTuit, message)
                    getComments(idTuit) // Refresca los comentarios
                } catch (e: Exception) {
                    // Puedes manejar el error si quieres
                }
            }
        }
    }

sealed interface CommentsState {
    object Loading : CommentsState

    data class Success(val comments: List<Tuit>) : CommentsState

    data class Error(val message: String) : CommentsState
}
