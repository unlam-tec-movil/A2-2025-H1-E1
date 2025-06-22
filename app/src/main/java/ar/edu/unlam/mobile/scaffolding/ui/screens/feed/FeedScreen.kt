package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.ui.components.PostItem
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    controller: NavHostController,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val posts = remember {
        mutableStateListOf(
            Post(1, 1, "Título 1", "Este es el contenido del post 1.", "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"),
            Post(2, 1, "Título 2", "Este es el contenido del post 2.", "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"),
            Post(3, 1, "Título 3", "Este es el contenido del post 3."),
            Post(4, 1, "Título 4", "Este es el contenido del post 4.", "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"),
            Post(5, 1, "Título 5", "Este es el contenido del post 5.")
        )
    }

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