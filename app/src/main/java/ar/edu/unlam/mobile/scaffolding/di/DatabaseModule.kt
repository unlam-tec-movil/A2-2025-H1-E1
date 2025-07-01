package ar.edu.unlam.mobile.scaffolding.di

import android.content.Context
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.DraftDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.FavoriteDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.UserDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.DraftRepository
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.UserRepository
import ar.edu.unlam.mobile.scaffolding.data.repositories.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    @Singleton
    fun provideDraftDao(db: AppDatabase): DraftDao = db.draftDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository = FavoriteRepository(dao)

    @Provides
    @Singleton
    fun provideDraftRepository(dao: DraftDao): DraftRepository = DraftRepository(dao)

    @Provides
    @Singleton
    fun provideUserRepository(dao: UserDao): UserRepository = UserRepository(dao)
}
