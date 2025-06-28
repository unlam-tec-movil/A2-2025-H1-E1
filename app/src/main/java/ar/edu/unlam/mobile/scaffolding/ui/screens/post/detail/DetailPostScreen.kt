package ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.ui.components.PostItem
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.FeedViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green

@Composable
fun DetailPostScreen(
    controller: NavHostController,
    idPost: Int,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val postState = viewModel.posts.collectAsStateWithLifecycle()
    val commentState = detailPostViewModel.comments.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")


    // Cargar comentarios cuando el token esté disponible
    LaunchedEffect(idPost, token) {
        if (token.isNotEmpty()) {
            detailPostViewModel.getComments(idPost, token)
        }
    }

    // Cargar posts si no están disponibles
    LaunchedEffect(token) {
        if (token.isNotEmpty() && postState.value is PostUiState.Loading) {
            viewModel.getPosts(token)
        }
    }

    val homeBackStackEntry =
        remember(controller.currentBackStackEntry) {
            controller.getBackStackEntry("home")
        }
    val favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = homeBackStackEntry)

    when (val state = postState.value) {
        is PostUiState.Error -> Text("Error: ${state.message}")
        PostUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(ar.edu.unlam.mobile.scaffolding.ui.theme.Green),
        ) {
            post?.let {
                PostItem(
                    post = it,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                    onLikeClick = { viewModel.onLikeClicked(it) },
                    currentUserId = currentUserId
                )
            } ?: run {
                // Si no se encuentra el post, mostrar mensaje
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Post no encontrado",
                        textAlign = TextAlign.Center,
                        color = Green,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(ar.edu.unlam.mobile.scaffolding.ui.theme.Green),
            ) {
                post?.let {
                    ar.edu.unlam.mobile.scaffolding.ui.components.PostItem(
                        post = it,
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                        navController = controller,
                        favoriteViewModel = favoriteViewModel,
                    )
                }
            } else {
                ListPost(
                    posts = filteredComments,
                    navController = controller,
                    favoriteViewModel = favoriteViewModel,
                    onLikeClick = { viewModel.onLikeClicked(it) },
                    currentUserId = currentUserId
                )
            }
        }
    }
}

@Composable
fun InputComment(modifier: Modifier = Modifier) {
    var comment by remember { mutableStateOf("") }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Green),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = {
                Text(
                    "Escribe un comentario...",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            },
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            maxLines = 3,
            singleLine = false,
            textStyle =
                TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                ),
        )

        IconButton(
            onClick = {
            },
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Enviar comentario",
            )
        }
    }
}
