package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val homeBackStackEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("home")
    }

    val favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = homeBackStackEntry)
    val favorites = favoriteViewModel.favorites.collectAsState()

    ListPost(
        posts = favorites.value,
        navController = navController,
        favoriteViewModel = favoriteViewModel
    )
}



