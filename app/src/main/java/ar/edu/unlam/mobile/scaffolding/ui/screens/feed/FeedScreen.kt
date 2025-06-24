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

@Composable
fun FeedScreen(
    modifier: Modifier,
    controller: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val postState = viewModel.posts.collectAsStateWithLifecycle()

    when (val state = postState.value) {
        is PostUiState.Error -> Text("Error: ${state.message}")
        PostUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is PostUiState.Success -> {
            ListPost(posts = state.list, navController = controller)
        }
    }
}
