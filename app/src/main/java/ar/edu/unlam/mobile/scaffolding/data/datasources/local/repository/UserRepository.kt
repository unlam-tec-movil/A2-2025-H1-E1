package ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository

import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.UserDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity
import javax.inject.Inject

class UserRepository
    @Inject
    constructor(private val userDao: UserDao) {
        suspend fun insertUser(userEntity: UserEntity) {
            userDao.insertUser(userEntity)
        }
    }
