package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.FollowDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.database.UserDao
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FollowEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FollowRepository
    @Inject
    constructor(
        private val followDao: FollowDao,
        private val userDao: UserDao,
    ) {
        suspend fun followUser(
            followerEmail: String,
            followedEmail: String,
            followedUserName: String = "",
            followedUserAvatar: String = "",
        ) {
            val followEntity = FollowEntity(followerEmail, followedEmail)
            followDao.followUser(followEntity)

            val userEntity =
                UserEntity(
                    email = followedEmail,
                    name = followedUserName.ifEmpty { followedEmail },
                    avatarUrl = followedUserAvatar,
                )
            saveUser(userEntity)
        }

        suspend fun unfollowUser(
            followerEmail: String,
            followedEmail: String,
        ) {
            val followEntity = FollowEntity(followerEmail, followedEmail)
            followDao.unfollowUser(followEntity)
        }

        suspend fun isFollowing(
            followerEmail: String,
            followedEmail: String,
        ): Boolean {
            return followDao.isFollowing(followerEmail, followedEmail)
        }

        suspend fun getFollowedCount(followerEmail: String): Int {
            return followDao.getFollowedCount(followerEmail)
        }

        suspend fun getFollowersCount(followedEmail: String): Int {
            return followDao.getFollowersCount(followedEmail)
        }

        suspend fun getFollowedUsers(followerEmail: String): List<UserProof> {
            val users = followDao.getFollowedUsersWithDetails(followerEmail)
            return users.map { user ->
                UserProof(
                    name = user.name,
                    username = user.email,
                    isFollowing = true,
                    avatarUrl = user.avatarUrl,
                )
            }
        }

        suspend fun getFollowers(followedEmail: String): List<UserProof> {
            val users = followDao.getFollowersWithDetails(followedEmail)
            return users.map { user ->
                UserProof(
                    name = user.name,
                    username = user.email,
                    isFollowing = false,
                    avatarUrl = user.avatarUrl,
                )
            }
        }

        suspend fun saveUser(user: UserEntity) {
            userDao.insertUser(user)
        }
    }
