package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.PostItem
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Composable
fun FeedScreen(
    modifier: Modifier,
    controller: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val postState = viewModel.posts.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")

    val homeBackStackEntry =
        remember(controller.currentBackStackEntry) {
            controller.getBackStackEntry("home")
        }

    val favoriteViewModel: FavoriteViewModel = hiltViewModel(homeBackStackEntry)

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.getPosts(token)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (val state = postState.value) {
            is PostUiState.Error -> Text("Error: ${state.message}")
            PostUiState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is PostUiState.Success -> {
                FeedContent(
                    posts = state.list,
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                    onLikeClick = { viewModel.onLikeClicked(it) },
                    currentUserId = currentUserId,
                    onLoadMore = { viewModel.loadMorePosts() },
                    hasMorePages = viewModel.hasMorePages(),
                    isLoadingMore = viewModel.isLoadingMore(),
                )
            }
        }
    }
}

@Composable
fun FeedContent(
    posts: List<ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit>,
    navController: NavHostController,
    favoriteViewModel: FavoriteViewModel,
    onLikeClick: (ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit) -> Unit,
    currentUserId: String,
    onLoadMore: () -> Unit,
    hasMorePages: Boolean,
    isLoadingMore: Boolean,
) {
    val listState = rememberLazyListState()

    // Detectar cuando el usuario está cerca del final de la lista
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = posts.size
            lastVisibleItem >= totalItems - 3 && hasMorePages && !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().background(color = GrayLight),
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                navController = navController,
                favoriteViewModel = favoriteViewModel,
                onLikeClick = onLikeClick,
                currentUserId = currentUserId,
            )
        }

        // Mostrar indicador de carga al final si hay más páginas
        if (hasMorePages) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isLoadingMore) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
