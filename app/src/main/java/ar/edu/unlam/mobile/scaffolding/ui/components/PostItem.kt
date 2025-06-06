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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import coil.compose.rememberAsyncImagePainter


@Composable
fun PostItem(post: Post, modifier: Modifier, navController: NavController ) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(modifier) {
            HeaderPostItem("Pepito123", "mailDePrueba", null)
            TitlePostItem(post.title)
            BodyPostItem(post.body)
            if (!post.urlPostImage.isNullOrEmpty()) {
                ImagePostItem(post.urlPostImage)
            }

            ButtonsPost(post.likes, post.comments, navController, post.id)
        }
    }
}


@Composable
fun ButtonsPost(likes: Int?, comments: Int?, navController: NavController, id: Int) {

    var isLiked by remember { mutableStateOf(false) }
    var isBookmark by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.weight(0.1f))

        IconButton(onClick = {
            isLiked = !isLiked
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Me gusta",
                    tint = if (isLiked) Green else Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "$likes",
                    color = GrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = {

                navController.navigate("comments/${id}")


        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Comment,
                    "Comentar",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "$comments",
                    color = GrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
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
                modifier = Modifier.size(30.dp)
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
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun TitlePostItem(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(top = 3.dp),
        maxLines = 1,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp

    )
}


@Composable
fun BodyPostItem(body: String) {
    Text(text = body, modifier = Modifier.padding(vertical = 2.dp), fontSize = 16.sp)
}

@Composable
fun HeaderPostItem(userName: String, userMail: String, userImage: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AvatarItem()
        Column(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
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
        fontSize = 12.sp
    )
}

@Composable
fun UserName(userName: String) {
    Text(
        text = "$userName",
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    )
}


@Composable
fun ListPost(posts: List<Post>, navController: NavController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = GrayLight)
    ) {
        items(posts) { post ->
            PostItem(post, modifier = Modifier.padding(vertical = 20.dp, horizontal = 25.dp), navController)
            Spacer(modifier = Modifier.padding(vertical = 2.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewListPost() {

    val posts = remember {
        mutableStateListOf(
            Post(
                1,
                1,
                "Título 1",
                "Este es el contenido del post 1.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"
            ),
            Post(
                2,
                1,
                "Título 2",
                "Este es el contenido del post 2.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"
            ),
            Post(
                3,
                1,
                "Título 3",
                "Este es el contenido del post 3."
            ),
            Post(
                4,
                1,
                "Título 4",
                "Este es el contenido del post 4.",
                "https://i0.wp.com/puppis.blog/wp-content/uploads/2022/02/abc-cuidado-de-los-gatos-min.jpg?resize=521%2C346&ssl=1"
            ),
            Post(
                5,
                1,
                "Título 4",
                "Este es el contenido del post 4."
            )
        )
    }

    val navController= rememberNavController()

    ListPost(posts, navController)

}