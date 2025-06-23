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
//    val posts =
//        remember {
//            mutableStateListOf(
//                Post(
//                    1,
//                    1,
//                    "Título 1",
//                    "Este es el contenido del post 1.",
//                    "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
//                ),
//                Post(
//                    2,
//                    1,
//                    "Título 2",
//                    "Este es el contenido del post 2.",
//                    "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
//                ),
//                Post(
//                    3,
//                    1,
//                    "Título 3",
//                    "Este es el contenido del post 3.",
//                ),
//                Post(
//                    4,
//                    1,
//                    "Título 4",
//                    "Este es el contenido del post 4.",
//                    "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1",
//                ),
//                Post(
//                    5,
//                    1,
//                    "Título 4",
//                    "Este es el contenido del post 4.",
//                ),
//            )
//        }

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
