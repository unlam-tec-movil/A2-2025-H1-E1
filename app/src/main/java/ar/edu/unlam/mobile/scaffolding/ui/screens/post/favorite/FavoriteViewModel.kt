package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<List<Tuit>>(emptyList())
    val favorites: StateFlow<List<Tuit>> = _favorites.asStateFlow()

    fun toggleFavorite(post: Tuit) {
        _favorites.value =
            if (_favorites.value.any { it.id == post.id }) {
                _favorites.value.filterNot { it.id == post.id }
            } else {
                _favorites.value + post
            }
    }

    fun isFavorite(postId: Int): Boolean {
        return _favorites.value.any { it.id == postId }
    }
}

