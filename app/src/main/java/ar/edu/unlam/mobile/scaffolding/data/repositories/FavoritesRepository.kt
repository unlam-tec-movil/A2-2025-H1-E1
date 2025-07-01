package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.FavoriteDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository
    @Inject
    constructor(
        private val dao: FavoriteDao,
    ) {
        fun getAllFavorites(): Flow<List<FavoriteEntity>> = dao.getAllFavorites()

        suspend fun addToFavorites(fav: FavoriteEntity) = dao.addFavorite(fav)

        suspend fun removeFromFavorites(fav: FavoriteEntity) = dao.removeFavorite(fav)

        suspend fun isFavorite(postId: Int): Boolean = dao.isFavorite(postId)
    }
