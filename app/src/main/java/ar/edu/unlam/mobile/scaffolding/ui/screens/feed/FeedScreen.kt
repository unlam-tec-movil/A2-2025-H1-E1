package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel

@Composable
fun FeedScreen(
    modifier: Modifier,
    controller: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val feedItems = viewModel.feedPagingData.collectAsLazyPagingItems()


    val homeBackStackEntry =
        remember(controller.currentBackStackEntry) {
            controller.getBackStackEntry("home")
        }

    val favoriteViewModel: FavoriteViewModel = hiltViewModel(homeBackStackEntry)


    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(feedItems.itemCount) { index ->
            val tuit = feedItems[index]
            tuit?.let {
                ListPost(
                    post = it,
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                )
            }
        }

        feedItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                }

                loadState.refresh is LoadState.Error -> {
                    item {
                        val e = loadState.refresh as LoadState.Error
                        Text("Error al cargar: ${e.error.message}")
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }

                loadState.append is LoadState.Error -> {
                    item {
                        val e = loadState.append as LoadState.Error
                        Text("Error al cargar más: ${e.error.message}")
                    }
                }
            }
        }
    }

}
