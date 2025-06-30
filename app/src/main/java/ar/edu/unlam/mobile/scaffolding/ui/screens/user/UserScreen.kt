package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState.*
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import coil.compose.AsyncImage

@Composable
fun UserScreen(
    userId: String = "User",
    controller: NavHostController = rememberNavController(),
    viewModel: UserViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userState by viewModel.user.collectAsStateWithLifecycle()
    
    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)
        }
    }

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

        Box(
            modifier =
                Modifier
                    .offset(y = (-40).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            val avatarUrl = (userState as? Success)?.user?.avatarUrl
            AsyncImage(
                model = avatarUrl ?: R.drawable.profile_photo,
                contentDescription = "Imagen de perfil",
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(0.1.dp, Color.White, CircleShape),
            )

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
                        .clickable { controller.navigate("edit profile") },
            )
        }

        when (val state = userState) {
            is Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            is Success -> {
                val user = state.user
                Text(
                    text = user.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 30.sp),
                )
                Text(
                    text = user.email,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                )
            }

            is Error -> {
                Text(
                    text = "Error: ${state.message}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Red),
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Spacer(modifier = Modifier.width(50.dp))
            Column {
                Text("3", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Post", color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(70.dp))
            Column {
                Text("20", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Seguidores", color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(70.dp))
            Column {
                Text("453", modifier = Modifier.align(Alignment.CenterHorizontally))
                Text("Seguidos", color = Color.Gray)
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4B877A)),
        )

        Spacer(modifier = Modifier.height(30.dp))
        Spacer(modifier = Modifier.height(200.dp))
    }
}
