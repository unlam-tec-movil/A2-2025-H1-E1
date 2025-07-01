package ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository

import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.DraftDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import javax.inject.Inject

class DraftRepository
    @Inject
    constructor(private val draftDao: DraftDao) {
        suspend fun insertDraft(draftEntity: DraftEntity) {
            draftDao.insertDraft(draftEntity)
        }

        suspend fun deleteDraft(draftEntity: DraftEntity) {
            draftDao.deleteDraft(draftEntity)
        }

        suspend fun getDraftsByUser(email: String): List<DraftEntity> {
            return draftDao.getDraftsByUser(email)
        }
    }
