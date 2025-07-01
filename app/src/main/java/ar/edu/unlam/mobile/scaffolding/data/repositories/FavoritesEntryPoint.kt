package ar.edu.unlam.mobile.scaffolding.data.repositories

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoritesEntryPoint {
    fun favoriteRepository(): FavoriteRepository
}
