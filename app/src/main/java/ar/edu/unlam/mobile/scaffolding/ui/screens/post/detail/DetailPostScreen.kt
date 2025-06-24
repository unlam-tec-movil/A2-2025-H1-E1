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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.models.Comment
import ar.edu.unlam.mobile.scaffolding.ui.components.ListComment
import ar.edu.unlam.mobile.scaffolding.ui.components.PostItem
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green

@Composable
fun DetailPostScreen(
    controller: NavHostController,
    idPost: Int,
) {
    val comments =
        remember {
            mutableStateListOf(
                Comment(1, 1, 1, "Comentario del post numero 1"),
                Comment(2, 1, 1, "Comentario del post numero 1"),
                Comment(3, 1, 2, "Comentario del post numero 2"),
                Comment(4, 1, 3, "Comentario del post numero 3"),
                Comment(5, 1, 1, "Comentario del post numero 1"),
            )
        }

    val posts =
        remember {
            mutableStateListOf(
                Tuit(
                    id = 1,
                    message = "Título 1",
                    parentId = 0,
                    author = "imanol",
                    avatarUrl = "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg",
                    likes = 10,
                    liked = true,
                    date = "2025-06-23",
                ),
                Tuit(
                    id = 2,
                    message = "Título 2",
                    parentId = 0,
                    author = "imanol",
                    avatarUrl = "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg",
                    likes = 8,
                    liked = false,
                    date = "2025-06-22",
                ),
                Tuit(
                    id = 3,
                    message = "Título 3",
                    parentId = 0,
                    author = "imanol",
                    avatarUrl = "",
                    likes = 0,
                    liked = false,
                    date = "2025-06-21",
                ),
            )
        }

    val post = posts.find { it.id == idPost }

    val filteredComments = comments.filter { it.idPost == idPost }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Green),
    ) {
        post?.let {
            PostItem(
                post = it,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 25.dp),
                controller = controller,
                isFavorite = false, // Se mantiene como falso, luego se debe integrar la lógica real
                onToggleFavorite = {},
            )
        } ?: run {
            Text("Post no encontrado", color = Color.White, modifier = Modifier.padding(16.dp))
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
            ListComment(
                comments = filteredComments,
                modifier = Modifier.weight(1f),
            )
        }

        InputComment(
            modifier = Modifier.padding(0.dp),
        )
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
                // Lógica para enviar comentario (actualiza el estado)
            },
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Enviar comentario",
            )
        }
    }
}
