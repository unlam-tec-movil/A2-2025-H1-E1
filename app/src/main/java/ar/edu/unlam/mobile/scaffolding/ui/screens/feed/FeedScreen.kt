package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    controller: NavHostController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    feedViewModel: FeedViewModel = hiltViewModel(),
) {
    // Obtener el estado de los posts desde el ViewModel
    val postState = feedViewModel.posts.collectAsStateWithLifecycle()

    when (val state = postState.value) {
        is PostUiState.Error -> {
            // Mostrar error
            Text("Error: ${state.message}")
        }
        PostUiState.Loading -> {
            // Mostrar indicador de carga
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is PostUiState.Success -> {
            // Mostrar lista de posts
            ListPost(posts = state.list, navController = controller)
        }
    }
}
