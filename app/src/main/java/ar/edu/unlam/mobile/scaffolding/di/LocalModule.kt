package ar.edu.unlam.mobile.scaffolding.di

import android.content.Context
import androidx.room.Room
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.DraftDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.UserDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.DraftRepository
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "tuiter_db").build()

    @Provides
    @Singleton
    fun provideDraftDao(db: AppDatabase): DraftDao = db.draftDao()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideDraftRepository(draftDao: DraftDao): DraftRepository = DraftRepository(draftDao)

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepository(userDao)
}
