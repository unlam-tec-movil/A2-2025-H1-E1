package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity

// TODO: Clase Room Database que expone DAOs.
@Database(entities = [DraftEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun draftDao(): DraftDao
    abstract fun userDao(): UserDao
}
