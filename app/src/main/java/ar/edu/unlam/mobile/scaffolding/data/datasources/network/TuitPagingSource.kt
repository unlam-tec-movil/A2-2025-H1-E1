package ar.edu.unlam.mobile.scaffolding.data.datasources.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ar.edu.unlam.mobile.scaffolding.BuildConfig
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import javax.inject.Inject

class TuitPagingSource @Inject constructor(
    private val api: ProfileApiService,
    private val applicationToken: String,
    private val userToken: String,
    private val onlyParents: Boolean
) :
    PagingSource<Int, Tuit>() {

    override fun getRefreshKey(state: PagingState<Int, Tuit>): Int? {
        return state.anchorPosition
    }

    //TODO ONLY PARENTS
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tuit> {
        val page = params.key ?: 1
        return try {
            val result = api.getFeed(
                userToken = userToken,
                token = applicationToken,
                page = page,
                parents = onlyParents
            )

            val nextKey = if (result.isEmpty()) {
                null
            } else {
                page + 1
            }

            LoadResult.Page(
                data = result,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}