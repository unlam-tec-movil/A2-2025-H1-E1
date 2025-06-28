package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.PostApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.TuitRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import javax.inject.Inject

class PostRespository
@Inject
constructor(
    private val postApiService: PostApiService,
) {
    // TODO: Implementa PostRepository usando datos remotos y locales.

    val applicationToken = BuildConfig.API_KEY

    suspend fun postTuit(
        message: String,
        userToken: String,
    ): Result<Unit> =
        postApiService.postTuit(
            userToken = userToken,
            token = applicationToken,
            request = TuitRequest(message),
        )

    suspend fun getTuits(
        id: Int,
        userToken: String,
    ): List<Tuit> =
        postApiService.getTuits(
            tuitId = id,
            userToken = userToken,
            token = applicationToken,
        )

    suspend fun likeTuit(
        id: Int,
        userToken: String,
    ) = postApiService.likeTuit(
        tuitId = id,
        userToken = userToken,
        token = applicationToken,
    )

    suspend fun unlikeTuit(
        id: Int,
        userToken: String,
    ) = postApiService.unlikeTuit(
        tuitId = id,
        userToken = userToken,
        token = applicationToken,
    )

    suspend fun getReplies(
        id: Int,
        userToken: String,
    ): List<Tuit> =
        postApiService.getReplies(
            tuitId = id,
            userToken = userToken,
            token = applicationToken,
        )

    suspend fun replyToTuit(
        id: Int,
        message: String,
        userToken: String,
    ) = postApiService.replyToTuit(
        tuitId = id,
        userToken = userToken,
        token = applicationToken,
        request = TuitRequest(message),
    )
}
