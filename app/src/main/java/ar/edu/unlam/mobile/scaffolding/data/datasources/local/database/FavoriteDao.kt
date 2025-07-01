package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import androidx.room.*
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :postId)")
    suspend fun isFavorite(postId: Int): Boolean
}
