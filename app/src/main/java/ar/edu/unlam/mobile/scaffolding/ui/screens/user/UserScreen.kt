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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Composable
fun UserScreen(
    userId: String = "User",
    viewModel: UserViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
    ) {
        // Header verde
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
                        .clickable(onClick = { /* Acción */ }),
            )
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "editar perfil",
                modifier =
                    Modifier
                        .size(45.dp)
                        .align(Alignment.BottomEnd)
                        .offset(6.dp, 6.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable(onClick = { }),
            )
        }

        when (val state = profileState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            is ProfileUiState.Success -> {
                val profile = state.profile
                Text(
                    text = profile.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(fontSize = 30.sp),
                )
                Text(
                    text = profile.email,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp),
                )
            }

            is ProfileUiState.Error -> {
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

        Spacer(modifier = Modifier.height(200.dp))

        FloatingActionButton(
            onClick = { },
            modifier =
                Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
                    .clip(CircleShape),
            containerColor = Color(0xFF4B877A),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo post", tint = Color.White)
        }
    }
}
