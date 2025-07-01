package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FavoriteEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) : ViewModel() {
        private val _favorites = MutableStateFlow<List<Tuit>>(emptyList())
        val favorites: StateFlow<List<Tuit>> = _favorites.asStateFlow()

        init {
            viewModelScope.launch {
                repository.getAllFavorites().collect { list ->
                    _favorites.value = list.map { it.toTuit() }
                }
            }
        }

        fun toggleFavorite(post: Tuit) {
            viewModelScope.launch {
                val isFav = repository.isFavorite(post.id)
                if (isFav) {
                    repository.removeFromFavorites(post.toEntity())
                } else {
                    repository.addToFavorites(post.toEntity())
                }
            }
        }

        fun isFavorite(postId: Int): Boolean {
            return _favorites.value.any { it.id == postId }
        }

        // Mapper de Room a dominio
        private fun FavoriteEntity.toTuit(): Tuit =
            Tuit(
                id = id,
                message = message,
                parentId = parentId,
                author = author,
                avatarUrl = avatarUrl,
                likes = likes,
                liked = liked,
                date = date,
            )

        // Mapper de dominio a Room
        private fun Tuit.toEntity(): FavoriteEntity =
            FavoriteEntity(
                id = id,
                message = message,
                parentId = parentId,
                author = author,
                avatarUrl = avatarUrl,
                likes = likes,
                liked = liked,
                date = date,
            )
    }
