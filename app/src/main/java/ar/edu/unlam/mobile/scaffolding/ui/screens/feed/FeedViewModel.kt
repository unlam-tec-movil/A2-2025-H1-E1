package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.PostRepository
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
    @Inject
    constructor(
        private val profileRepository: ProfileRepository,
        private val postRepository: PostRepository,
    ) : ViewModel() {
        // Para Paging
        val feedPagingData: Flow<PagingData<Tuit>> =
            profileRepository.getFeedPagingData().cachedIn(viewModelScope)

        // Para el estado manual de post (como en develop)
        private val _posts = MutableStateFlow<PostUiState>(PostUiState.Loading)
        val posts: StateFlow<PostUiState> get() = _posts

        private var userToken: String = ""

        fun getPosts(userToken: String) {
            this.userToken = userToken
            viewModelScope.launch {
                try {
                    val posts = profileRepository.getFeed()
                    _posts.value = PostUiState.Success(posts)
                } catch (e: Exception) {
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handlePostError(e)
                    _posts.value = PostUiState.Error(errorMsg)
                }
            }
        }

        fun onLikeClicked(tuit: Tuit) {
            viewModelScope.launch {
                try {
                    val liked = !tuit.liked

                    if (liked) {
                        postRepository.likeTuit(tuit.id, userToken)
                    } else {
                        postRepository.unlikeTuit(tuit.id, userToken)
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
    }

sealed interface PostUiState {
    object Loading : PostUiState

    data class Success(val list: List<Tuit>) : PostUiState

    data class Error(val message: String) : PostUiState
}
