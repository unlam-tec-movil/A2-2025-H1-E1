package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorites-db",
                ).build().also { instance = it }
            }
    }
}
