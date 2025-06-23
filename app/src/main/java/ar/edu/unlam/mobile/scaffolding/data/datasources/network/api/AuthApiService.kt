package ar.edu.unlam.mobile.scaffolding.data.datasources.network.api

import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.LoginRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.UserRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.LoginResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.UserResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/login")
    suspend fun login(
        @Header("Application-Token") token: String,
        @Body request: LoginRequest,
    ): LoginResponse

    @POST("/api/v1/users")
    suspend fun users(
        @Header("Application-Token") token: String,
        @Body request: UserRequest,
    ): UserResponse
}
