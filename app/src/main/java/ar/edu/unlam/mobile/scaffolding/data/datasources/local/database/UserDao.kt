package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import androidx.room.Dao
import androidx.room.Upsert
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity

@Dao
interface UserDao {
    // TODO: Interfaz DAO para manejo de usuarios favoritos u offline.

    @Upsert
    suspend fun insertUser(user: UserEntity)
}
