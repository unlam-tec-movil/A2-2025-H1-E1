package ar.edu.unlam.mobile.scaffolding.data.datasources.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/v1/login")
    suspend fun login(
        @Body request: LoginRequest,
    ): LoginResponse

    // @GET("/api/v1/me/profile")
    // suspend fun login(@Header ): LoginResponse
}
