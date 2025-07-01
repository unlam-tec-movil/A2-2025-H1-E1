package ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (favorites.value.isEmpty()) {
            Text(
                text = "No tienes posts guardados",
                color = Color.Gray,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            ListPost(
                posts = favorites.value,
                navController = navController,
                favoriteViewModel = favoriteViewModel,
                onLikeClick = { viewModel.onLikeClicked(it) },
                modifier = modifier
            )
        }
    }
}