package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import coil.compose.rememberAsyncImagePainter

@Composable
fun PostItem(
    post: Tuit,
    modifier: Modifier = Modifier,
    controller: NavController,
    isFavorite: Boolean,
    onToggleFavorite: (Tuit) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            HeaderPostItem(
                userName = post.author,
                userMail = post.author,
                userImage = post.avatarUrl,
            )

            Spacer(modifier = Modifier.height(8.dp))

            post.avatarUrl.let {
                ImagePostItem(imageUrl = it)
                Spacer(modifier = Modifier.height(8.dp))
            }

            BodyPostItem(body = post.message)

            ButtonsPost(
                likes = post.likes,
                comments = 0, // Simulado por ahora
                navController = controller,
                id = post.id,
                isInitiallyLiked = post.liked,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = { onToggleFavorite(post) }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Guardar",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.Gray,
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonsPost(
    likes: Int?,
    comments: Int?,
    navController: NavController,
    id: Int,
    isInitiallyLiked: Boolean = false,
) {
    var isLiked by remember { mutableStateOf(isInitiallyLiked) }
    var isBookmark by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(onClick = { isLiked = !isLiked }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Me gusta",
                    tint = if (isLiked) Green else Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    text = "${likes ?: 0}",
                    color = GrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            navController.navigate("comments/$id")
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Comment,
                    "Comentar",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    text = "${comments ?: 0}",
                    color = GrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {
            isBookmark = !isBookmark
        }) {
            Icon(
                Icons.Default.Bookmark,
                "Guardar",
                tint = if (isBookmark) Green else Color.Gray,
                modifier = Modifier.size(30.dp),
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))
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
fun BodyPostItem(body: String) {
    Text(text = body, modifier = Modifier.padding(vertical = 2.dp), fontSize = 16.sp)
}

@Composable
fun HeaderPostItem(
    userName: String,
    userMail: String,
    userImage: String?,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AvatarItem()
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
            UserMail(userMail)
        }
    }
}

@Composable
fun UserMail(userMail: String) {
    Text(
        text = "@$userMail",
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        color = GrayLight,
        fontSize = 12.sp,
    )
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
    isFavorite: (Tuit) -> Boolean = { false },
    onToggleFavorite: (Tuit) -> Unit = {},
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
                controller = navController,
                isFavorite = isFavorite(post),
                onToggleFavorite = onToggleFavorite,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListPost() {
    val fakePosts =
        listOf(
            Tuit(
                id = 1,
                message = "Kiair",
                parentId = 0,
                author = "abueladedante",
                avatarUrl = "https://cdn-icons-png.flaticon.com/512/147/147144.png",
                likes = 5,
                liked = true,
                date = "2025-06-23",
            ),
            Tuit(
                id = 2,
                message = "Dantima",
                parentId = 0,
                author = "cacaman.rpg",
                avatarUrl = "https://cdn-icons-png.flaticon.com/512/147/147144.png",
                likes = 12,
                liked = false,
                date = "2025-06-22",
            ),
            Tuit(
                id = 3,
                message = "gaubrera",
                parentId = 0,
                author = "yairciño",
                avatarUrl = "",
                likes = 0,
                liked = false,
                date = "2025-06-20",
            ),
        )

    val navController = rememberNavController()

    ListPost(
        posts = fakePosts,
        navController = navController,
        isFavorite = { it.liked },
        onToggleFavorite = {},
    )
}
