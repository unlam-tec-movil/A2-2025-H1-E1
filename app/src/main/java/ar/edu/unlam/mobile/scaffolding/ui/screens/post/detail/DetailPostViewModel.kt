package ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRespository
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler
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

        private val _sendCommentState = MutableStateFlow<SendCommentState>(SendCommentState.Idle)
        val sendCommentState: StateFlow<SendCommentState> get() = _sendCommentState

        private val _post = MutableStateFlow<PostUiState>(PostUiState.Loading)
        val post: StateFlow<PostUiState> get() = _post

        private var userToken: String = ""

        fun getComments(
            idTuit: Int,
            userToken: String,
        ) {
            this.userToken = userToken
            viewModelScope.launch {
                try {
                    _comments.value = CommentsState.Loading
                    val result = postRespository.getReplies(idTuit, userToken)
                    _comments.value = CommentsState.Success(result)
                } catch (e: Exception) {
                    _comments.value = CommentsState.Error(ErrorHandler.handlePostError(e))
                }
            }
        }

        fun getPostById(
            postId: Int,
            token: String,
        ) {
            this.userToken = token
            viewModelScope.launch {
                try {
                    _post.value = PostUiState.Loading
                    val result = postRespository.getTuits(postId, token)
                    _post.value = PostUiState.Success(listOf(result))
                } catch (e: Exception) {
                    val errorMsg = ErrorHandler.handlePostError(e)
                    _post.value = PostUiState.Error(errorMsg)
                }
            }
        }

        fun sendComment(
            idTuit: Int,
            message: String,
        ) {
            viewModelScope.launch {
                try {
                    _sendCommentState.value = SendCommentState.Loading
                    postRespository.replyToTuit(idTuit, message, userToken)
                    _sendCommentState.value = SendCommentState.Success
                    getComments(idTuit, userToken) // Refresca los comentarios
                } catch (e: Exception) {
                    _sendCommentState.value = SendCommentState.Error(ErrorHandler.handleCommentError(e))
                }
            }
        }

        fun clearSendCommentState() {
            _sendCommentState.value = SendCommentState.Idle
        }

        fun onLikeClicked(tuit: Tuit) {
            viewModelScope.launch {
                try {
                    val liked = !tuit.liked

                    if (liked) {
                        postRespository.likeTuit(tuit.id, userToken)
                    } else {
                        postRespository.unlikeTuit(tuit.id, userToken)
                    }

                    // Actualizar si el like fue sobre el post principal
                    val currentPostList = (_post.value as? PostUiState.Success)?.list
                    if (currentPostList != null) {
                        val updatedList =
                            currentPostList.map {
                                if (it.id == tuit.id) {
                                    it.copy(
                                        liked = liked,
                                        likes = if (liked) it.likes + 1 else it.likes - 1,
                                    )
                                } else {
                                    it
                                }
                            }
                        _post.value = PostUiState.Success(updatedList)
                    }

                    // También actualizamos los comentarios si corresponde
                    val currentComments = (_comments.value as? CommentsState.Success)?.comments
                    if (currentComments != null) {
                        val updatedComments =
                            currentComments.map {
                                if (it.id == tuit.id) {
                                    it.copy(
                                        liked = liked,
                                        likes = if (liked) it.likes + 1 else it.likes - 1,
                                    )
                                } else {
                                    it
                                }
                            }
                        _comments.value = CommentsState.Success(updatedComments)
                    }
                } catch (e: Exception) {
                    val errorMsg = ErrorHandler.handlePostError(e)
                    _comments.value = CommentsState.Error(errorMsg)
                }
            }
        }
    }

sealed interface CommentsState {
    object Loading : CommentsState

    data class Success(val comments: List<Tuit>) : CommentsState

    data class Error(val message: String) : CommentsState
}

sealed interface SendCommentState {
    object Idle : SendCommentState

    object Loading : SendCommentState

    object Success : SendCommentState

    data class Error(val message: String) : SendCommentState
}
