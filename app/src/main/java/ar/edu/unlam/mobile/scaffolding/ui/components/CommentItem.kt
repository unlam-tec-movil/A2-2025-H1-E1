package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile.scaffolding.data.models.Comment
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier) {
            HeaderPostItem("Pepito123", "mailDePrueba", null)
            BodyPostItem(comment.body)
            ButtonLike(comment.likes)
        }
    }
}

@Composable
fun ButtonLike(likes: Int?) {
    var isLiked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(onClick = {
            isLiked = !isLiked
        }, modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Me gusta",
                    tint = if (isLiked) Green else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    text = "$likes",
                    color = GrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Composable
fun ListComment(
    comments: List<Comment>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = GrayLight),
    ) {
        items(comments) { comment ->
            CommentItem(
                comment,
                modifier = Modifier.padding(top = 8.dp, start = 25.dp, end = 25.dp),
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListCommets() {
    val comments =
        remember {
            mutableStateListOf(
                Comment(
                    1,
                    1,
                    1,
                    "Comentario del post numero 1",
                ),
                Comment(
                    2,
                    1,
                    1,
                    "Comentario del post numero 1",
                ),
                Comment(
                    3,
                    1,
                    1,
                    "Comentario del post numero 1",
                ),
                Comment(
                    4,
                    1,
                    1,
                    "Comentario del post numero 1",
                ),
                Comment(
                    5,
                    1,
                    1,
                    "Comentario del post numero 1",
                ),
            )
        }

    ListComment(comments)
}
