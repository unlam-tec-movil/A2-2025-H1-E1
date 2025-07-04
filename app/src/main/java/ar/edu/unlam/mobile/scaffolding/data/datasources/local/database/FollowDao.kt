package ar.edu.unlam.mobile.scaffolding.data.datasources.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.FollowEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.UserEntity

@Dao
interface FollowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun followUser(follow: FollowEntity)

    @Delete
    suspend fun unfollowUser(follow: FollowEntity)

    @Query("SELECT * FROM follows WHERE followerEmail = :followerEmail")
    suspend fun getFollowedUsers(followerEmail: String): List<FollowEntity>

    @Query("SELECT * FROM follows WHERE followedEmail = :followedEmail")
    suspend fun getFollowers(followedEmail: String): List<FollowEntity>

    @Query("SELECT COUNT(*) FROM follows WHERE followerEmail = :followerEmail")
    suspend fun getFollowedCount(followerEmail: String): Int

    @Query("SELECT COUNT(*) FROM follows WHERE followedEmail = :followedEmail")
    suspend fun getFollowersCount(followedEmail: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerEmail = :followerEmail AND followedEmail = :followedEmail)")
    suspend fun isFollowing(
        followerEmail: String,
        followedEmail: String,
    ): Boolean

    @Query(
        """
        SELECT u.* FROM users u
        INNER JOIN follows f ON u.email = f.followedEmail
        WHERE f.followerEmail = :followerEmail
    """,
    )
    suspend fun getFollowedUsersWithDetails(followerEmail: String): List<UserEntity>

    @Query(
        """
        SELECT u.* FROM users u
        INNER JOIN follows f ON u.email = f.followerEmail
        WHERE f.followedEmail = :followedEmail
    """,
    )
    suspend fun getFollowersWithDetails(followedEmail: String): List<UserEntity>
}
