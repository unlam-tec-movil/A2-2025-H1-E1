package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.data.repositories.FollowRepository
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState.*
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import coil.compose.AsyncImage
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FollowRepositoryEntryPoint {
    fun followRepository(): FollowRepository
}

@Composable
fun UserScreen(
    controller: NavHostController = rememberNavController(),
    viewModel: UserViewModel = hiltViewModel(),
    feedViewModel: FeedViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val followRepository =
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            FollowRepositoryEntryPoint::class.java,
        ).followRepository()
    val postState = feedViewModel.posts.collectAsStateWithLifecycle()

    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userState by viewModel.user.collectAsStateWithLifecycle()

    val currentUserIdState = userStore.leerDatosUsuario.collectAsState(initial = "")
    val currentUserId = currentUserIdState.value

    // Estados para seguimiento
    var followersCount by remember { mutableStateOf(0) }
    var followedCount by remember { mutableStateOf(0) }
    var isFollowing by remember { mutableStateOf(false) }
    var isCurrentUser by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val scrollOffset = if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 300
    val collapseRange = 300f // Puedes ajustar este valor para el rango de colapso
    val collapseFraction = (scrollOffset / collapseRange).coerceIn(0f, 1f)

    val headerHeight by animateDpAsState(targetValue = (184 - 100 * collapseFraction).dp)
    val avatarSize by animateDpAsState(targetValue = (120 - 70 * collapseFraction).dp)
    val avatarOffsetY by animateDpAsState(targetValue = ((-40) + 30 * collapseFraction).dp)
    val headerAlpha by animateFloatAsState(targetValue = 1f - collapseFraction)
    val miniHeaderAlpha by animateFloatAsState(targetValue = collapseFraction)

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

    // Cargar datos de seguimiento cuando cambie el usuario
    LaunchedEffect(userState, currentUserId) {
        if (userState is Success && currentUserId.isNotEmpty()) {
            val user = (userState as Success).user
            isCurrentUser = user.email == currentUserId

            if (!isCurrentUser) {
                // Si no es el usuario actual, verificar si lo está siguiendo
                isFollowing = followRepository.isFollowing(currentUserId, user.email)
            }

            // Cargar contadores
            followersCount = followRepository.getFollowersCount(user.email)
            followedCount = followRepository.getFollowedCount(user.email)
        }
    }

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
                // Avatar grande y lápiz superpuesto
                if (headerAlpha > 0.01f) {
                    val avatarUrl = (userState as? Success)?.user?.avatarUrl
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.BottomCenter),
                    ) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Imagen de perfil",
                            modifier =
                                Modifier
                                    .size(avatarSize)
                                    .clip(CircleShape)
                                    .border(0.1.dp, Color.White, CircleShape)
                                    .alpha(headerAlpha),
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.profile_photo),
                            fallback = painterResource(R.drawable.profile_photo),
                        )
                        // Botón editar solo si es el usuario actual
                        if (isCurrentUser) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "Editar perfil",
                                modifier =
                                    Modifier
                                        .size((45 - 25 * collapseFraction).dp)
                                        .align(Alignment.BottomEnd)
                                        .offset(x = 10.dp, y = 10.dp)
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            controller.navigate("edit profile")
                                        }
                                        .alpha(headerAlpha),
                            )
                        }
                    }
                }
            }
        }
        // Mini barra sticky (aparece al scrollear y queda fija arriba)
        stickyHeader {
            if (userState is Success) {
                val user = (userState as Success).user
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
                        model = user.avatarUrl,
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
                        text = user.name,
                        style = TextStyle(fontSize = 20.sp),
                        color = Color.Black,
                    )
                }
            }
        }
        // Info de usuario (desaparece al scrollear)
        item {
            when (val state = userState) {
                is Loading ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center).alpha(headerAlpha),
                        )
                    }
                is Success -> {
                    val user = state.user
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .alpha(headerAlpha),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = user.name,
                            style = TextStyle(fontSize = 30.sp),
                        )
                        Text(
                            text = user.email,
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                        )
                    }
                }
                is Error ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Error: ${state.message}",
                            modifier = Modifier.align(Alignment.Center).alpha(headerAlpha),
                            style = TextStyle(color = Color.Red),
                        )
                    }
            }
        }
        // Contadores (desaparecen al scrollear)
        item {
            val user = (userState as? Success)?.user
            val userPosts =
                user?.let {
                    when (val state = postState.value) {
                        is PostUiState.Success -> state.list.filter { post -> post.author == user.name }
                        else -> emptyList()
                    }
                } ?: emptyList()
            Spacer(modifier = Modifier.height(20.dp).alpha(headerAlpha))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .alpha(headerAlpha),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(userPosts.size.toString())
                    Text("Post", color = Color.Gray)
                }
                Column(
                    modifier =
                        Modifier.clickable {
                            if (isCurrentUser) {
                                controller.navigate("followed")
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        if (isCurrentUser) followedCount.toString() else followersCount.toString(),
                    )
                    Text(if (isCurrentUser) "Seguidos" else "Seguidores", color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(20.dp).alpha(headerAlpha))
            Spacer(
                modifier =
                    Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(color = GrayLight)
                        .alpha(headerAlpha),
            )
        }
        // Lista de tuits
        if (userState is Success) {
            val user = (userState as Success).user
            val filteredPosts =
                when (val state = postState.value) {
                    is PostUiState.Success -> state.list.filter { it.author == user.name }
                    else -> emptyList()
                }
            items(filteredPosts) { post ->
                ListPost(
                    posts = listOf(post),
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                    onLikeClick = { feedViewModel.onLikeClicked(it) },
                    modifier = Modifier.fillMaxWidth(),
                    currentUserId = currentUserId,
                )
            }
        }
        item { Spacer(modifier = Modifier.height(30.dp)) }
        item { Spacer(modifier = Modifier.height(200.dp)) }
    }
}
