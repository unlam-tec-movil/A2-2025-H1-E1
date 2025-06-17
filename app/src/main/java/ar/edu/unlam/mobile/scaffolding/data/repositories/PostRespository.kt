package ar.edu.unlam.mobile.scaffolding.data.repositories

import androidx.multidex.BuildConfig
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
        val userToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJlbWFpbCI6InVzdWFyaW9AZ21haWwuY29tIiwiZXhwIjoxNzUyNTMzNjM1LCJpc3MiOiJ1bmxhbS10dWl0ZXIiLCJuYW1lIjoidXN1YXJpb0BnbWFpbC5jb20iLCJzdWIiOjIwOH0." +
                "r1fdwZBUQMvHZgQurqPNpVVJSmNmRRWXnMY-U04e-ew"

        suspend fun postTuit(message: String): Result<Unit> =
            postApiService.postTuit(
                userToken = userToken,
                token = applicationToken,
                request = TuitRequest(message),
            )

        suspend fun getTuits(id: Int): List<Tuit> = postApiService.getTuits(tuitId = id, userToken = userToken, token = applicationToken)

        suspend fun likeTuit(id: Int) = postApiService.likeTuit(tuitId = id, userToken = userToken, token = applicationToken)

        suspend fun unlikeTuit(id: Int) =
            postApiService.unlikeTuit(
                tuitId = id,
                userToken = userToken,
                token = applicationToken,
            )

        suspend fun getReplies(id: Int): List<Tuit> =
            postApiService.getReplies(
                tuitId = id,
                userToken = userToken,
                token = applicationToken,
            )

        suspend fun replyToTuit(
            id: Int,
            message: String,
        ) = postApiService.replyToTuit(
            tuitId = id,
            userToken = userToken,
            token = applicationToken,
            request = TuitRequest(message),
        )
    }
