package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    ListPost(
        posts =
            viewModel.favoritePosts.map { post ->
                Tuit(
                    id = post.id,
                    message = post.body,
                    parentId = 0,
                    author = "Unknown",
                    avatarUrl = "",
                    likes = post.likes ?: 0,
                    liked = false,
                    date = "2025-01-01",
                )
            },
        navController = navController,
        isFavorite = { tuit ->
            // Pasamos un Post sólo con id porque es lo que importa para la lógica
            viewModel.isFavorite(Post(id = tuit.id, userId = 0, title = "", body = ""))
        },
        onToggleFavorite = { tuit ->
            viewModel.toggleFavorite(Post(id = tuit.id, userId = 0, title = "", body = ""))
        },
    )
}
