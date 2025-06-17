package ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    val name: String,
    val password: String,
    @SerializedName("avatar_url") val avatarUrl: String,
)
