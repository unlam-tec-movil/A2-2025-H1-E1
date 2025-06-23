package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.components.PostItem
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    controller: NavHostController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    feedViewModel: FeedViewModel = hiltViewModel()
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
    val posts = remember {
        mutableStateListOf(
            Post(
                1,
                1,
                "Título 1",
                "Este es el contenido del post 1.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
            ),
            Post(
                2,
                1,
                "Título 2",
                "Este es el contenido del post 2.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
            ),
            Post(3, 1, "Título 3", "Este es el contenido del post 3."),
            Post(
                4,
                1,
                "Título 4",
                "Este es el contenido del post 4.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
            ),
            Post(5, 1, "Título 5", "Este es el contenido del post 5."),
        )
    }

    // Mostrar los posts usando un LazyColumn
    LazyColumn {
        items(posts) { post ->
            PostItem(
                post = post,
                controller = controller,
                isFavorite = favoriteViewModel.isFavorite(post),
                onToggleFavorite = { favoriteViewModel.toggleFavorite(it) }
            )
        }
    }
}
