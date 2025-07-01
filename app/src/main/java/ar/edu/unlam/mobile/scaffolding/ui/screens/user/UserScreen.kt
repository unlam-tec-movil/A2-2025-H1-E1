package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState.*
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import coil.compose.AsyncImage

@Composable
fun UserScreen(
    controller: NavHostController = rememberNavController(),
    viewModel: UserViewModel = hiltViewModel(),
    feedViewModel: FeedViewModel = hiltViewModel(),
) {
    val postState = feedViewModel.posts.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userState by viewModel.user.collectAsStateWithLifecycle()

    val currentUserIdState = userStore.leerDatosUsuario.collectAsState(initial = "")
    val currentUserId = currentUserIdState.value

    val homeBackStackEntry =
        remember(controller.currentBackStackEntry) {
            controller.getBackStackEntry("home")
        }

    val favoriteViewModel: FavoriteViewModel = hiltViewModel(homeBackStackEntry)

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)
            feedViewModel.getPosts(token)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Editar Perfil",
            modifier =
                Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
        )

        val isCurrentUser = (userState as? Success)?.user?.id == currentUserId

        Box(
            modifier =
                Modifier
                    .offset(y = (-40).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            val avatarUrl = (userState as? Success)?.user?.avatarUrl
            AsyncImage(
                model = avatarUrl ?: R.drawable.profile_photo,
                contentDescription = "Imagen de perfil",
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(0.1.dp, Color.White, CircleShape),
            )

            Image(
                painter =
                    painterResource(
                        id = if (isCurrentUser) R.drawable.ic_edit else R.drawable.unlamlogo,
                    ),
                contentDescription = if (isCurrentUser) "Editar perfil" else "Seguir usuario",
                modifier =
                    Modifier
                        .size(45.dp)
                        .align(Alignment.BottomEnd)
                        .offset(6.dp, 6.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            if (isCurrentUser) {
                                // Acción para editar perfil
                            } else {
                                // Acción para seguir usuario
                            }
                        },
            )
        }

        when (val state = userState) {
            is Loading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

            is Success -> {
                val user = state.user
                Text(
                    text = user.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 30.sp),
                )
                Text(
                    text = user.email,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                )
            }

            is Error ->
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Red),
                )
        }

        val user = (userState as? Success)?.user

        val userPosts =
            user?.let {
                when (val state = postState.value) {
                    is PostUiState.Success -> state.list.filter { post -> post.author == user.name }
                    else -> emptyList()
                }
            } ?: emptyList()

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(userPosts.size.toString(), modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Post", color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text("453", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Seguidos", color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))
        Spacer(
            modifier =
                Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = GrayLight),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            when (val state = postState.value) {
                is PostUiState.Error -> Text("Error: ${state.message}")

                PostUiState.Loading ->
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                is PostUiState.Success -> {
                    if (userState is Success) {
                        val user = (userState as Success).user
                        val filteredPosts = state.list.filter { it.author == user.name }

                        ListPost(
                            posts = filteredPosts,
                            navController = controller,
                            favoriteViewModel = favoriteViewModel,
                            onLikeClick = { feedViewModel.onLikeClicked(it) },
                            modifier = Modifier.weight(1f),
                            currentUserId = currentUserId,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(200.dp))
    }
}
