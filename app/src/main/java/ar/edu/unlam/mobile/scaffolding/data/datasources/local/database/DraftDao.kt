package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity

@Dao
interface DraftDao {
    @Upsert
    suspend fun insertDraft(draft: DraftEntity)

    @Delete
    suspend fun deleteDraft(draft: DraftEntity)

    @Query("SELECT * FROM draft_posts WHERE userEmail = :email")
    suspend fun getDraftsByUser(email: String): List<DraftEntity>
}
