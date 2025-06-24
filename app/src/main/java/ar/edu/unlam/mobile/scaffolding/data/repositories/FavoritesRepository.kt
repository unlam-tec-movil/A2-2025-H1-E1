package ar.edu.unlam.mobile.scaffolding.data.repositories

import androidx.compose.runtime.mutableStateListOf
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository
    @Inject
    constructor() {
        private val _favorites = mutableStateListOf<Post>()
        val favorites: List<Post> get() = _favorites

        fun toggle(post: Post) {
            val idx = _favorites.indexOfFirst { it.id == post.id }
            if (idx >= 0) {
                _favorites.removeAt(idx)
            } else {
                _favorites.add(post)
            }
        }

        fun isFavorite(post: Post): Boolean = _favorites.any { it.id == post.id }
    }
