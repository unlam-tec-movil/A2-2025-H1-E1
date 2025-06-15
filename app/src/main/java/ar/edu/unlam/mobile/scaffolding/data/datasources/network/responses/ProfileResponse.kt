package ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    val name: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    val email: String
)