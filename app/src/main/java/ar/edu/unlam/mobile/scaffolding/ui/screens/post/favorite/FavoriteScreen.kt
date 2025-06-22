package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    controller: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    ListPost(posts = viewModel.favoritePosts, controller = controller)
}

