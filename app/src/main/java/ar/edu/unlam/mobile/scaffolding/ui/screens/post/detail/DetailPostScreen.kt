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
import androidx.compose.runtime.LaunchedEffect
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
    detailPostViewModel: DetailPostViewModel = hiltViewModel(),
) {
    val postState = viewModel.posts.collectAsStateWithLifecycle()
    val commentState = detailPostViewModel.comments.collectAsStateWithLifecycle()

    LaunchedEffect(idPost) {
        detailPostViewModel.getComments(idPost)
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

        is PostUiState.Success -> {
            val post = state.list.find { it.id == idPost }
            val filteredComments = when (val comments = commentState.value) {
                is CommentsState.Success -> comments.comments
                else -> emptyList()
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
                        onLikeClick = {viewModel.onLikeClicked(it)}
                    )
                }

                Text(
                    text = "Comentarios",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier =
                        Modifier
                            .padding(vertical = 8.dp)
                            .padding(start = 20.dp),
                )

                if (filteredComments.isEmpty()) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Sin Comentarios",
                            textAlign = TextAlign.Center,
                            color = Green,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                } else {
                    ListPost(
                        posts = filteredComments,
                        navController = controller,
                        favoriteViewModel = favoriteViewModel,
                        onLikeClick = { viewModel.onLikeClicked(it) },
                    )
                }

                InputComment(
                    modifier = Modifier.padding(0.dp),
                    idPost = idPost,
                    detailPostViewModel = detailPostViewModel
                )
            }
        }
    }
}

@Composable
fun InputComment(
    modifier: Modifier = Modifier,
    idPost: Int,
    detailPostViewModel: DetailPostViewModel
) {
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
                if (comment.isNotBlank()) {
                    detailPostViewModel.sendComment(idPost, comment)
                    comment = "" // Limpiar campo
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Enviar comentario",
            )
        }
    }
}
