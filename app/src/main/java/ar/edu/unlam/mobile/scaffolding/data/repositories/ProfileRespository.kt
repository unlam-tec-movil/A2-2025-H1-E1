package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.ProfileRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import javax.inject.Inject

class ProfileRespository @Inject constructor(private val profileApiService: ProfileApiService) {
    // TODO: Implementa UserRepository para perfil, favoritos, etc.

    val applicationToken = BuildConfig.API_KEY
    val userToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InVzdWFyaW9AZ21haWwuY29tIiwiZXhwIjoxNzUyNTMzNjM1LCJpc3MiOiJ1bmxhbS10dWl0ZXIiLCJuYW1lIjoidXN1YXJpb0BnbWFpbC5jb20iLCJzdWIiOjIwOH0.r1fdwZBUQMvHZgQurqPNpVVJSmNmRRWXnMY-U04e-ew"


    suspend fun getProfile(): ProfileResponse {
        return profileApiService.getProfile(userToken = userToken, token = applicationToken)
    }

    suspend fun updateProfile(name: String, password: String, avatarUrl: String) {
        return profileApiService.updateProfile(
            token = applicationToken,
            request = ProfileRequest(name, password, avatarUrl)
        )
    }


    suspend fun getFeed(): List<Tuit> {
        return profileApiService.getFeed(
            userToken = userToken,
            token = applicationToken,
            page = 1,
            parents = false
        )
    }


}
