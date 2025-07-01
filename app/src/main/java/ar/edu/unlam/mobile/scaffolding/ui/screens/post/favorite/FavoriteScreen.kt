package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val homeBackStackEntry =
        remember(navController.currentBackStackEntry) {
            navController.getBackStackEntry("home")
        }

    val favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = homeBackStackEntry)
    val favorites = favoriteViewModel.favorites.collectAsState()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")


    ListPost(
        posts = favorites.value,
        navController = navController,
        favoriteViewModel = favoriteViewModel,
        onLikeClick = { viewModel.onLikeClicked(it) },
        modifier,
        currentUserId = currentUserId
    )
}
