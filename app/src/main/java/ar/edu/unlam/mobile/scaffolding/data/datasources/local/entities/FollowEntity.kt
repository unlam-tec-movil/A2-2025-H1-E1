package ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities

import androidx.room.Entity

@Entity(
    tableName = "follows",
    primaryKeys = ["followerEmail", "followedEmail"],
)
data class FollowEntity(
    val followerEmail: String,
    val followedEmail: String,
    val followedAt: Long = System.currentTimeMillis(),
)
