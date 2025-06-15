package ar.edu.unlam.mobile.scaffolding.data.datasources.network.api

import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.ProfileRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface ProfileApiService {
    @GET("/api/v1/me/profile")
    suspend fun getProfile(
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
    ): ProfileResponse

    @PUT("/api/v1/me/profile")
    suspend fun updateProfile(
        @Header("Application-Token") token: String,
        @Body request: ProfileRequest
    )

    @GET("api/v1/me/feed")
    suspend fun getFeed(
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
        @Query("page") page: Int,
        @Query("only_parents") parents: Boolean
    ): List<Tuit>

}