package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FavoriteEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FollowEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity

@Database(entities = [FavoriteEntity::class, DraftEntity::class, UserEntity::class, FollowEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    abstract fun draftDao(): DraftDao

    abstract fun userDao(): UserDao

    abstract fun followDao(): FollowDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        """
                    CREATE TABLE IF NOT EXISTS `follows` (
                        `followerEmail` TEXT NOT NULL,
                        `followedEmail` TEXT NOT NULL,
                        `followedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`followerEmail`, `followedEmail`)
                    )
                """,
                    )
                }
            }

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tuiter_db",
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }
    }
}
