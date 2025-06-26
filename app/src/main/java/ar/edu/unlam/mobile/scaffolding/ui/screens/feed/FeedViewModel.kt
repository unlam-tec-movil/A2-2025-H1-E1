package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
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
        private val profileRepository: ProfileRespository,
    ) : ViewModel() {
        // TODO: ViewModel para manejar el estado del feed.

        private val _posts = MutableStateFlow<PostUiState>(PostUiState.Loading)
        val posts: StateFlow<PostUiState> get() = _posts

    val feedPagingData: Flow<PagingData<Tuit>> =
        profileRepository.getFeedPagingData()
            .cachedIn(viewModelScope)


        init {
            getPosts()
        }

        private fun getPosts() {
            viewModelScope.launch {
                try {
                    _posts.value = PostUiState.Success(profileRepository.getFeed())
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
