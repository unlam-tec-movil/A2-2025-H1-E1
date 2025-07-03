package ar.edu.unlam.mobile.scaffolding.ui.screens.user.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun UserProfileScreen(
    userId: String,
    userName: String,
    avatarUrl: String,
    controller: NavHostController,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF4B877A)),
        )

        Box(
            modifier =
                Modifier
                    .offset(y = (-40).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Imagen de perfil",
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(0.1.dp, Color.White, CircleShape),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = userName,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 30.sp),
        )

//        Text(
//            text = "ID: $userId",
//            modifier = Modifier.align(Alignment.CenterHorizontally),
//            style = TextStyle(color = Color.Gray, fontSize = 16.sp)
//        )

        Spacer(modifier = Modifier.height(20.dp))

        // Aquí puedes agregar más contenido si quieres
        Text(
            text = "Este es el perfil público del usuario.",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 16.sp),
        )
    }
}
