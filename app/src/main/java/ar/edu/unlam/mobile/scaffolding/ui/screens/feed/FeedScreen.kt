package ar.edu.unlam.mobile.scaffolding.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.components.ListPost
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import kotlinx.coroutines.launch

@Composable
fun FeedScreen(
    modifier: Modifier,
    controller: NavHostController,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val postState = viewModel.posts.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.getPosts(token)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Botón de cerrar sesión
        Button(
            onClick = {
                coroutineScope.launch {
                    userStore.escribirDatosUsuario("")
                    userStore.escribirEstadoLogin(false)
                    userStore.escribirTokenUsuario("")
                }
                controller.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                ),
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Cerrar sesión")
        }

        when (val state = postState.value) {
            is PostUiState.Error -> Text("Error: ${state.message}")
            PostUiState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is PostUiState.Success -> {
                ListPost(posts = state.list, navController = controller)
            }
        }
    }
}
