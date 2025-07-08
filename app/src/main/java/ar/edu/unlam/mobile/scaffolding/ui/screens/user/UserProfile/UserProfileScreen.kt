package ar.edu.unlam.mobile.scaffolding.ui.screens.user.userprofile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
    viewModel: UserViewModel = hiltViewModel(),
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
    val userPosts =
        remember(postState.value) {
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

    val listState = rememberLazyListState()
    val scrollOffset = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 300
    val collapseRange = 300f
    val collapseFraction = (scrollOffset / collapseRange).coerceIn(0f, 1f)

    val headerHeight by animateDpAsState(targetValue = (184 - 100 * collapseFraction).dp)
    val avatarSize by animateDpAsState(targetValue = (120 - 70 * collapseFraction).dp)
    val avatarOffsetY by animateDpAsState(targetValue = ((-40) + 30 * collapseFraction).dp)
    val headerAlpha by animateFloatAsState(targetValue = 1f - collapseFraction)
    val miniHeaderAlpha by animateFloatAsState(targetValue = collapseFraction)

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        // Cabecera grande (desaparece al scrollear)
        item {
            Box {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = "Editar Perfil",
                        modifier =
                            Modifier
                                .height(headerHeight)
                                .fillMaxWidth()
                                .alpha(headerAlpha),
                    )
                    Spacer(modifier = Modifier.height((60 * (1f - collapseFraction)).dp))
                }
                // Avatar grande SOLO visible si el header no está colapsado
                if (headerAlpha > 0.01f) {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Imagen de perfil",
                        modifier =
                            Modifier
                                .size(avatarSize)
                                .clip(CircleShape)
                                .border(0.1.dp, Color.White, CircleShape)
                                .align(Alignment.BottomCenter)
                                .alpha(headerAlpha),
                        contentScale = ContentScale.Crop,
                        error = painterResource(R.drawable.profile_photo),
                        fallback = painterResource(R.drawable.profile_photo),
                    )
                }
            }
        }
        // Mini barra sticky (aparece al scrollear y queda fija arriba)
        stickyHeader {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .alpha(miniHeaderAlpha),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar pequeño",
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(0.1.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.profile_photo),
                    fallback = painterResource(R.drawable.profile_photo),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = userName,
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.Black,
                )
            }
        }
        // Info de usuario (desaparece al scrollear)
        item {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .alpha(headerAlpha),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = userName,
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
                    modifier = Modifier.padding(horizontal = 32.dp),
                ) {
                    Text(if (isFollowing) "Dejar de seguir" else "Seguir")
                }
                Spacer(modifier = Modifier.height(20.dp))
                // Contador de seguidores
                Text(
                    text = "$followersCount seguidores",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Este es el perfil público del usuario.",
                    style = TextStyle(fontSize = 16.sp),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${userPosts.size} tuit${if (userPosts.size != 1) "s." else "."}",
                    style = TextStyle(fontSize = 16.sp),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        // Lista de tuits
        items(userPosts) { post ->
            ListPost(
                posts = listOf(post),
                navController = controller,
                favoriteViewModel = favoriteViewModel,
                onLikeClick = { feedViewModel.onLikeClicked(it) },
                modifier = Modifier.fillMaxWidth(),
                currentUserId = currentUserId,
            )
        }
        item { Spacer(modifier = Modifier.height(30.dp)) }
        item { Spacer(modifier = Modifier.height(200.dp)) }
    }
}
