package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel

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

    when (val state = postState.value) {
        is PostUiState.Error -> Text("Error: ${state.message}")
        PostUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is PostUiState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                ListPost(
                    posts = state.list,
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                    onLikeClick = { viewModel.onLikeClicked(it) },
                    currentUserId = currentUserId
                )
            }
        }
    }
}
