package ar.edu.unlam.mobile.scaffolding.ui.screens.user.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.data.repositories.FollowRepository
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserViewModel
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import coil.compose.AsyncImage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FollowRepositoryEntryPoint {
    fun followRepository(): FollowRepository
}

@Composable
fun UserProfileScreen(
    userId: String,
    userName: String,
    avatarUrl: String,
    controller: NavHostController,
    feedViewModel: FeedViewModel = hiltViewModel(),
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val followRepository =
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            FollowRepositoryEntryPoint::class.java,
        ).followRepository()

    val userStore = remember { UserStore(context) }
    val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()


    val postState = feedViewModel.posts.collectAsStateWithLifecycle()
    val userPosts = remember(postState.value) {
        when (val state = postState.value) {
            is PostUiState.Success -> state.list.filter { it.author == userName }
            else -> emptyList()
        }
    }

    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            feedViewModel.getPosts(token)
        }
    }


    // Estados para seguimiento
    var isFollowing by remember { mutableStateOf(false) }
    var followersCount by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    // Cargar datos de seguimiento
    LaunchedEffect(currentUserId, userId) {
        if (currentUserId.isNotEmpty() && userId.isNotEmpty()) {
            isFollowing = followRepository.isFollowing(currentUserId, userId)
            followersCount = followRepository.getFollowersCount(userId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {

        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Editar Perfil",
            modifier =
                Modifier
                    .height(184.dp)
                    .fillMaxWidth(),
        )

        Box(
            modifier =
                Modifier
                    .offset(y = (-40).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(0.1.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.profile_photo),
                fallback = painterResource(R.drawable.profile_photo)
            )
        }

        Text(
            text = userName,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 30.sp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Botón de seguir/dejar de seguir
        Button(
            onClick = {
                scope.launch {
                    if (isFollowing) {
                        followRepository.unfollowUser(currentUserId, userId)
                        isFollowing = false
                    } else {
                        followRepository.followUser(currentUserId, userId, userName, avatarUrl)
                        isFollowing = true
                    }
                    followersCount = followRepository.getFollowersCount(userId)
                }
            },
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 32.dp),
        ) {
            Text(if (isFollowing) "Dejar de seguir" else "Seguir")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contador de seguidores
        Text(
            text = "$followersCount seguidores",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 16.sp, color = Color.Gray),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Este es el perfil público del usuario.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 16.sp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${userPosts.size} tuit${if (userPosts.size != 1) "s." else "."}",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 16.sp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            when (val state = postState.value) {
                is PostUiState.Error -> Text("Error: ${state.message}")
                PostUiState.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is PostUiState.Success -> {
                    val userPosts = state.list.filter { post -> post.author == userName }

                    ListPost(
                        posts = userPosts,
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
}
