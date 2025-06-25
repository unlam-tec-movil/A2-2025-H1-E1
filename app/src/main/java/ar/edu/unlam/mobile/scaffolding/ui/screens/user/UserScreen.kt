package ar.edu.unlam.mobile.scaffolding.ui.screens.user


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R

@Preview()
@Composable
fun UserScreen(
    userId: String = "U23112ak34",
    controller: NavHostController = rememberNavController(),
    viewmodel : UserViewModel = hiltViewModel()) {

    val userState = viewmodel.user.collectAsStateWithLifecycle()

    val name = when (val state = userState.value) {
        is UserUiState.Success -> state.user.name
        else -> "Usuario desconocido"
    }

    val currentUserId = "U23112ak34"

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF4B877A)),
        )

        // Foto de perfil
        Box(
            modifier =
                Modifier
                    .offset(y = (-40).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_photo),
                contentDescription = "Profile photo",
                modifier =
                    Modifier
                        .size(95.dp)
                        .clip(CircleShape)
                        .border(0.1.dp, Color.White, CircleShape)
                        .clickable(onClick = {}),
            )

            if (userId == currentUserId) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar Perfil",
                    modifier =
                        Modifier
                            .size(45.dp)
                            .align(Alignment.BottomEnd)
                            .offset(6.dp, 6.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .clickable(onClick = { controller.navigate("edit profile") }),
                )
            }
            else {
                Image(
                    painter = painterResource(id = R.drawable.unlamlogo),
                    contentDescription = "Seguir",
                    modifier =
                        Modifier
                            .size(45.dp)
                            .align(Alignment.BottomEnd)
                            .offset(6.dp, 6.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .clickable(onClick = { /* PONER LOGICA PARA SEGUIR */ }),
                )
            }

        }

        Text(
            text = name,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(fontSize = 30.sp),
        )
        Text(
            text = "@$userId",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            "Descripcion del usuario",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Spacer(modifier = Modifier.width(50.dp))
            Column {
                Text(
                    "3",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    "Post",
                    color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.width(70.dp))
            Column {
                Text(
                    "20",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    "Seguidores",
                    color = Color.Gray,
                )
            }
            Spacer(modifier = Modifier.width(70.dp))
            Column {
                Text(
                    "453",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    "Seguidos",
                    color = Color.Gray,
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF4B877A)),
        )

        Spacer(modifier = Modifier.height(30.dp))

        Spacer(modifier = Modifier.height(200.dp))

    }
}
