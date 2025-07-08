package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRespository
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
    @Inject
    constructor(
        private val profileRepository: ProfileRespository,
        private val postRespository: PostRespository,
    ) : ViewModel() {
        private val _posts = MutableStateFlow<PostUiState>(PostUiState.Loading)
        val posts: StateFlow<PostUiState> get() = _posts
        private var userToken: String = ""

        // Variables para paginación
        private var currentPage = 1
        private var isLoadingMore = false
        private var hasMorePages = true

        fun getPosts(userToken: String) {
            this.userToken = userToken
            currentPage = 1
            hasMorePages = true
            viewModelScope.launch {
                try {
                    _posts.value = PostUiState.Loading
                    val posts = profileRepository.getFeedWithPagination(userToken, currentPage)
                    _posts.value = PostUiState.Success(posts)
                } catch (e: Exception) {
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handlePostError(e)
                    _posts.value = PostUiState.Error(errorMsg)
                }
            }
        }

        fun loadMorePosts() {
            if (isLoadingMore || !hasMorePages) return

            viewModelScope.launch {
                try {
                    isLoadingMore = true
                    currentPage++
                    val newPosts = profileRepository.getFeedWithPagination(userToken, currentPage)

                    if (newPosts.isEmpty()) {
                        hasMorePages = false
                    } else {
                        val currentPosts = (_posts.value as? PostUiState.Success)?.list ?: emptyList()
                        _posts.value = PostUiState.Success(currentPosts + newPosts)
                    }
                } catch (e: Exception) {
                    currentPage-- // Revertir el incremento en caso de error
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handlePostError(e)
                    _posts.value = PostUiState.Error(errorMsg)
                } finally {
                    isLoadingMore = false
                }
            }
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

                    val updatedList =
                        (_posts.value as? PostUiState.Success)?.list?.map {
                            if (it.id == tuit.id) {
                                it.copy(
                                    liked = liked,
                                    likes = if (liked) it.likes + 1 else it.likes - 1,
                                )
                            } else {
                                it
                            }
                        }

                    _posts.value = PostUiState.Success(updatedList ?: emptyList())
                } catch (e: Exception) {
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handlePostError(e)
                    _posts.value = PostUiState.Error(errorMsg)
                }
            }
        }

        fun hasMorePages(): Boolean = hasMorePages

        fun isLoadingMore(): Boolean = isLoadingMore
    }

sealed interface PostUiState {
    object Loading : PostUiState

    data class Success(
        val list: List<Tuit>,
    ) : PostUiState

    data class Error(
        val message: String,
    ) : PostUiState
}
