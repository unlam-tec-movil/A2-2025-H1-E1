package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.models.Post

class FavoriteViewModel : ViewModel() {
    private val _favoritePosts = mutableStateListOf<Post>()
    val favoritePosts: List<Post> = _favoritePosts

    fun toggleFavorite(post: Post) {
        if (_favoritePosts.any { it.id == post.id }) {
            _favoritePosts.removeIf { it.id == post.id }
        } else {
            _favoritePosts.add(post)
        }
    }

    fun isFavorite(post: Post): Boolean = _favoritePosts.any { it.id == post.id }
}
