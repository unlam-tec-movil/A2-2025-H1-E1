package ar.edu.unlam.mobile.scaffolding.ui.screens.post.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.ui.theme.SoftGreen
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Composable
fun DetailPostScreen(
    controller: NavHostController,
    idPost: Int,
    detailPostViewModel: DetailPostViewModel = hiltViewModel(),
) {
    val postState = detailPostViewModel.post.collectAsStateWithLifecycle()
    val commentState = detailPostViewModel.comments.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val currentUserId by userStore.leerDatosUsuario.collectAsState(initial = "")

    LaunchedEffect(idPost, token) {
        if (token.isNotEmpty()) {
            detailPostViewModel.getPostById(idPost, token)
            detailPostViewModel.getComments(idPost, token)
        }
    }

    val homeBackStackEntry =
        remember(controller.currentBackStackEntry) {
            controller.getBackStackEntry("home")
        }
    val favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = homeBackStackEntry)

    if (postState.value is PostUiState.Loading || commentState.value is CommentsState.Loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    if (postState.value is PostUiState.Error) {
        Box(Modifier.fillMaxSize()) {
            Text(
                "Error: ${(postState.value as PostUiState.Error).message}",
                modifier = Modifier.align(Alignment.Center),
            )
        }
        return
    }

    if (commentState.value is CommentsState.Error) {
        Box(Modifier.fillMaxSize()) {
            Text(
                "Error al cargar comentarios: ${(commentState.value as CommentsState.Error).message}",
                modifier = Modifier.align(Alignment.Center),
            )
        }
        return
    }

    if (postState.value is PostUiState.Success) {
        val post = (postState.value as PostUiState.Success).list.find { it.id == idPost }
        val filteredComments =
            when (val comments = commentState.value) {
                is CommentsState.Success -> comments.comments
                else -> emptyList()
            }

        Box(
            modifier = Modifier.fillMaxSize().background(Color.White),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(bottom = 70.dp),
            ) {
                post?.let {
                    PostItem(
                        post = it,
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                        navController = controller,
                        favoriteViewModel = favoriteViewModel,
                        onLikeClick = { detailPostViewModel.onLikeClicked(it) },
                        currentUserId = currentUserId,
                    )
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Post no encontrado",
                            textAlign = TextAlign.Center,
                            color = GrayLight,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp,
                        color = Color.LightGray,
                    )
                    Text(
                        text = "Comentarios",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp).padding(start = 20.dp),
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp,
                        color = Color.LightGray,
                    )

                    if (filteredComments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "Sin Comentarios",
                                textAlign = TextAlign.Center,
                                color = GrayLight,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    } else {
                        ListPost(
                            posts = filteredComments,
                            navController = controller,
                            favoriteViewModel = favoriteViewModel,
                            onLikeClick = { detailPostViewModel.onLikeClicked(it) },
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            currentUserId = currentUserId,
                        )
                    }
                }
            }

            InputComment(
                modifier = Modifier.align(Alignment.BottomCenter).background(SoftGreen),
                idPost = idPost,
                detailPostViewModel = detailPostViewModel,
            )
        }
    }
}

@Composable
fun InputComment(
    modifier: Modifier = Modifier,
    idPost: Int,
    detailPostViewModel: DetailPostViewModel,
) {
    var comment by remember { mutableStateOf("") }

    Row(
        modifier = modifier.fillMaxWidth().background(SoftGreen),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = {
                Text(
                    "Escribe un comentario...",
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                )
            },
            modifier = Modifier.weight(1f).padding(end = 8.dp),
            maxLines = 3,
            singleLine = false,
            textStyle =
                TextStyle(
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
        )

        IconButton(
            onClick = {
                if (comment.isNotBlank()) {
                    detailPostViewModel.sendComment(idPost, comment)
                    comment = ""
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Enviar comentario",
                tint = BlueGreen,
            )
        }
    }
}
