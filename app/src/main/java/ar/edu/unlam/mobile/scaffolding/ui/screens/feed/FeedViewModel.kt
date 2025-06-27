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

        fun getPosts(userToken: String) {
            this.userToken = userToken
            viewModelScope.launch {
                try {
                    _posts.value = PostUiState.Success(profileRepository.getFeed(userToken))
                } catch (e: Exception) {
                    _posts.value = PostUiState.Error(e.message ?: "Error desconocido")
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
                    _posts.value = PostUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
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
