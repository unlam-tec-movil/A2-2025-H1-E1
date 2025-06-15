package ar.edu.unlam.mobile.scaffolding.data.datasources.network.api

import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.TuitRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiService {

    @POST("/api/v1/me/tuits")
    suspend fun postTuit(
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
        @Body request: TuitRequest
    ): Result<Unit>

    @GET("/api/v1/me/tuits/{tuit_id}")
    suspend fun getTuits(
        @Path("tuit_id") tuitId: Int,
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
    ): List<Tuit>

    @POST("/api/v1/me/tuits/{tuit_id}/likes")
    suspend fun likeTuit(
        @Path("tuit_id") tuitId: Int,
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
    )

    @DELETE("/api/v1/me/tuits/{tuit_id}/likes")
    suspend fun unlikeTuit(
        @Path("tuit_id") tuitId: Int,
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
    )

    @GET("/api/v1/me/tuits/{tuit_id}/replies")
    suspend fun getReplies(
        @Path("tuit_id") tuitId: Int,
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
    ): List<Tuit>

    @POST("/api/v1/me/tuits/{tuit_id}/replies")
    suspend fun replyToTuit(
        @Path("tuit_id") tuitId: Int,
        @Header("Authorization") userToken: String,
        @Header("Application-Token") token: String,
        @Body request: TuitRequest
    )
}