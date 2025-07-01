package ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val message: String,
    val parentId: Int,
    val author: String,
    val avatarUrl: String,
    val likes: Long,
    val liked: Boolean,
    val date: String,
)
