package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.AuthApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.LoginRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.UserRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.LoginResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.UserResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    private val authApiService: AuthApiService,
) {
    val applicationToken = BuildConfig.API_KEY

    suspend fun login(
        email: String,
        password: String,
    ): LoginResponse =
        authApiService.login(
            token = applicationToken,
            request = LoginRequest(email, password),
        )

    suspend fun users(
        name: String,
        email: String,
        password: String,
    ): UserResponse =
        authApiService.users(
            token = applicationToken,
            request = UserRequest(name, email, password),
        )
}
