package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.unlam.mobile.scaffolding.R
import coil.compose.AsyncImage
import coil.request.ImageRequest

// TODO: Componente para ver imagen del usuario (Redondito).

@Composable
fun AvatarItem(
    avatarUrl: String? = null,
    size: Int = 50,
) {
    if (avatarUrl != null && avatarUrl.isNotEmpty()) {
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
            contentDescription = "User_image",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape),
            error = painterResource(R.drawable.profile_photo),
            placeholder = painterResource(R.drawable.profile_photo),
        )
    } else {
        Image(
            painter = painterResource(R.drawable.profile_photo),
            contentDescription = "User_image",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape),
        )
    }
}

@Preview
@Composable
fun AvatarItemPreview() {
    AvatarItem()
}
