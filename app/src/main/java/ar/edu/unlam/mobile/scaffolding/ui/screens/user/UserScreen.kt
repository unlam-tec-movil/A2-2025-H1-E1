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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.data.models.Post
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Preview()
@Composable
fun UserScreen(
    userId: String = "User gay",
    controller: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val currentUserIdState = userStore.leerDatosUsuario.collectAsState(initial = "")
    val currentUserId = currentUserIdState.value

    val isCurrentUser = userId == currentUserId

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)
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
                            .clickable(onClick = { /* Accion */ }),
                )
                Image(
                    painter = painterResource(
                        id = if (isCurrentUser) R.drawable.ic_edit else R.drawable.unlamlogo
                    ),
                    contentDescription = if (isCurrentUser) "Editar perfil" else "Seguir usuario",
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.BottomEnd)
                        .offset(6.dp, 6.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            if (isCurrentUser) {
                                /* Accion para editar perfil */
                            } else {
                                /* Accion para seguir usuario */
                            }
                        })
                )
            }
            Text(
                text = "User",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 30.sp),
            )
            Text(
                text = "@$userId",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                // style = TextStyle(color = Color.Gray)
            )
            Text(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
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

            // Posts del pibe
            //  ListPost(posts, controller)

            Spacer(modifier = Modifier.height(200.dp))

            if (isCurrentUser) {
                FloatingActionButton(
                    onClick = { /* Accin para nuevo post */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                        .clip(CircleShape),
                    containerColor = Color(0xFF4B877A),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo post", tint = Color.White)
                }
            }
        }
    }
}
