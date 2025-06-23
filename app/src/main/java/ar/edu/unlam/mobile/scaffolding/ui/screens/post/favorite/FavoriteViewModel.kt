package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.data.repositories.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repo: FavoritesRepository
) : ViewModel() {

    /**
     * Lista inmutable de favoritos, expuesta a la UI
     */
    val favoritePosts: List<Post>
        get() = repo.favorites

    /**
     * Alterna el estado de favorito de un post
     */
    fun toggleFavorite(post: Post) {
        repo.toggle(post)
    }

    /**
     * Consulta si un post está marcado como favorito
     */
    fun isFavorite(post: Post): Boolean =
        repo.isFavorite(post)
}

