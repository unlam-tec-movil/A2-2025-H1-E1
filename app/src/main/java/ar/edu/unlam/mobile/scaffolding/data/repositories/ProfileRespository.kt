package ar.edu.unlam.mobile.scaffolding.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.TuitPagingSource
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.ProfileRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileRespository
@Inject
constructor(
    private val profileApiService: ProfileApiService,
) {
    // TODO: Implementa UserRepository para perfil, favoritos, etc.

    val applicationToken = BuildConfig.API_KEY
    val userToken = BuildConfig.USER_TOKEN
    val onlyParents = false

    suspend fun getProfile(): ProfileResponse =
        profileApiService.getProfile(userToken = userToken, token = applicationToken)

    suspend fun updateProfile(
        name: String,
        password: String,
        avatarUrl: String,
    ) = profileApiService.updateProfile(
        token = applicationToken,
        request = ProfileRequest(name, password, avatarUrl),
    )

    suspend fun getFeed(): List<Tuit> =
        profileApiService.getFeed(
            userToken = userToken,
            token = applicationToken,
            page = 1,
            parents = false,
        )

    fun getFeedPagingData(): Flow<PagingData<Tuit>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TuitPagingSource(
                    api = profileApiService,
                    applicationToken = applicationToken,
                    userToken = userToken,
                    onlyParents = onlyParents

                    )
            }
        ).flow
    }
}

