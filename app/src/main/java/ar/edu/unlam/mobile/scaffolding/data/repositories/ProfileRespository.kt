package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.ProfileRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import javax.inject.Inject

class ProfileRespository
@Inject
constructor(
    private val profileApiService: ProfileApiService,
) {
    val applicationToken = BuildConfig.API_KEY

    suspend fun getProfile(userToken: String): ProfileResponse =
        profileApiService.getProfile(
            userToken = userToken,
            token = applicationToken,
        )

    suspend fun updateProfile(
        name: String,
        password: String,
        avatarUrl: String,
        userToken: String,
    ) = profileApiService.updateProfile(
        token = userToken,
        request = ProfileRequest(name, password, avatarUrl),
    )

    suspend fun getFeed(userToken: String): List<Tuit> =
        profileApiService.getFeed(
            userToken = userToken,
            token = applicationToken,
            page = 1,
            parents = false,
        )
}
