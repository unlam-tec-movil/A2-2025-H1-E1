package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen
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
    val currentBackStackEntry = navController.currentBackStackEntry
    val currentPostId = currentBackStackEntry?.arguments?.getInt("idPost")
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val isInCommentsScreen = currentRoute?.startsWith("comments/") == true


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
                onClick = { if (currentPostId != post.id && !isInCommentsScreen) {
                    navController.navigate("comments/${post.id}")
                }
                },
                enabled = currentPostId != post.id
            ) {

                Icon(
                    Icons.Default.Comment,
                    "Comentar",
                 //   tint = Color.Gray,
                    tint = if(currentPostId == post.id) BlueGreen else Color.Gray,
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
                onClick = {
                    favoriteViewModel.toggleFavorite(post)
                    navController.navigate("favorite")
                },
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

) {
    var clickCount by remember { mutableStateOf(0) }
    var isCooldown by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val currentBackStackEntry = navController.currentBackStackEntry
    val currentPostId = currentBackStackEntry?.arguments?.getInt("idPost")
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val isInCommentsScreen = currentRoute?.startsWith("comments/") == true


    fun onPostClick() {
        if (isCooldown) return

        clickCount += 1
        when (clickCount) {
            1 -> {
                // Espera a ver si es 1 solo click o 2
                scope.launch {
                    delay(300L)
                    if (clickCount == 1 && currentPostId != post.id && !isInCommentsScreen ) {

                        navController.navigate("comments/${post.id}")
                    } else if (clickCount == 2) {

                        onLikeClick(post)
                    }
                    clickCount = 0
                    isCooldown = true
                    delay(1000L)
                    isCooldown = false
                }
            }
            2 -> {
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { onPostClick() },
    ) {
        Column(modifier) {
            HeaderPostItem(post.author, post.avatarUrl)
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
    userName: String,
    userImage: String?,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
        text = "$userName",
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
    modifier: Modifier?,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(color = GrayLight),
    ) {
        items(posts) { post ->
            PostItem(
                post,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
                navController,
                favoriteViewModel = favoriteViewModel,
                onLikeClick = onLikeClick,
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}
