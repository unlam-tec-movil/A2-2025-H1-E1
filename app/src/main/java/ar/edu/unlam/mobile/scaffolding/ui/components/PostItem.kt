package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.ui.screens.post.favorite.FavoriteViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ButtonsPost(
    post: Tuit,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel,
    onLikeClick: (Tuit) -> Unit,
) {
    val isBookmarked = favoriteViewModel.isFavorite(post.id)

    fun formatLikes(likes: Long): String {
        return when {
            likes >= 100_000_000_000 -> "∞"
            likes >= 1_000_000_000 -> "${likes / 1_000_000_000}MilM"
            likes >= 1_000_000 -> "${likes / 1_000_000}M"
            likes >= 1_000 -> "${likes / 1_000}K"
            else -> likes.toString()
        }
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
    ) {
        // Filita de like
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(0.dp),
        ) {
            IconButton(
                onClick = { onLikeClick(post) },
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Me gusta",
                    tint = if (post.liked) Green else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
            }
            Text(
                text = "${formatLikes(post.likes)}",
                color = if (post.liked) Green else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        // Filita de Comentarios
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = { navController.navigate("comments/${post.id}") },
            ) {
                Icon(
                    Icons.Default.Comment,
                    "Comentar",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
            }
        }

        // Filita de de guardar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(
                onClick = { favoriteViewModel.toggleFavorite(post) },
            ) {
                Icon(
                    Icons.Default.Bookmark,
                    "Guardar",
                    tint = if (isBookmarked) Green else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Composable
fun PostItem(
    post: Tuit,
    modifier: Modifier,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel,
    onLikeClick: (Tuit) -> Unit,
    currentUserId: String,
) {
    var countLike by remember { mutableIntStateOf(0) }
    var isCooldown by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun likedAuto() {
        if (isCooldown) return

        countLike += 1
        if (countLike == 2) {
            onLikeClick(post)
            countLike = 0
            isCooldown = true

            scope.launch {
                delay(1000L)
                isCooldown = false
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { likedAuto() },
    ) {
        Column(modifier) {
            HeaderPostItem(
                userId = post.author,
                currentUserId = currentUserId,
                userName = post.author,
                userImage = post.avatarUrl,
                navController = navController,
            )
            BodyPostItem(post.message)
            ButtonsPost(post, navController, favoriteViewModel, onLikeClick)
        }
    }
}

@Composable
fun ImagePostItem(imageUrl: String) {
    val painter = rememberAsyncImagePainter(model = imageUrl)
    Image(
        painter = painter,
        contentDescription = "Imagen del post",
        modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun TitlePostItem(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(top = 3.dp),
        maxLines = 1,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
    )
}

@Composable
fun BodyPostItem(body: String) {
    Text(
        text = body,
        modifier = Modifier.padding(top = 7.dp, bottom = 2.dp, start = 5.dp),
        fontSize = 16.sp,
    )
}

@Composable
fun HeaderPostItem(
    userId: String,
    currentUserId: String,
    userName: String,
    userImage: String?,
    navController: NavController,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    if (userId != currentUserId) {
                        navController.navigate("user/$userId")
                    }
                },
    ) {
        AvatarItem(avatarUrl = userImage, size = 50)
        Column(
            modifier =
                Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            UserName(userName)
        }
    }
}

@Composable
fun UserName(userName: String) {
    Text(
        text = userName,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    )
}

@Composable
fun ListPost(
    posts: List<Tuit>,
    navController: NavController,
    favoriteViewModel: FavoriteViewModel,
    onLikeClick: (Tuit) -> Unit,
    currentUserId: String,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = GrayLight),
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                navController = navController,
                favoriteViewModel = favoriteViewModel,
                onLikeClick = onLikeClick,
                currentUserId = currentUserId,
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
