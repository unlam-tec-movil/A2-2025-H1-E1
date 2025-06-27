package ar.edu.unlam.mobile.scaffolding.ui.screens.post.controller

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.unlam.mobile.scaffolding.ui.components.AvatarItem
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

// TODO: ViewModel para manejar estado del posteo nuevo.
@Composable
fun ControllerPostScreen(
    modifier: Modifier = Modifier,
    viewModel: ControllerPostViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val state by viewModel.newPost.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userViewModel: UserViewModel = hiltViewModel()
    val profileState by userViewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            userViewModel.loadProfile(token)
        }
    }

    LaunchedEffect(state) {
        if (state is NewPostUiState.Success) {
            Log.i("Screen", "Success")
            navigateToHome()
        }
    }

    val onCloseClick = {
        navigateToHome()
    }

    val onPostClick = {
        viewModel.newPost(text, token)
    }

    val onTextChange = { newText: String ->
        text = newText
    }

    Scaffold { innerPading ->
        Column(modifier.padding(innerPading)) {
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = (Icons.Default.Close),
                        contentDescription = null,
                        modifier = modifier.size(28.dp),
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Green),
                    modifier = modifier.padding(end = 8.dp),
                    onClick = onPostClick,
                    enabled = text.isNotBlank() && state != NewPostUiState.Loading,
                ) { Text("Publicar", fontWeight = FontWeight.Bold) }
            }

            Row {
                when (val profileStateValue = profileState) {
                    is ar.edu.unlam.mobile.scaffolding.ui.screens.user.ProfileUiState.Success -> {
                        AvatarItem(avatarUrl = profileStateValue.profile.avatarUrl, size = 50)
                    }
                    else -> {
                        AvatarItem(size = 50)
                    }
                }
                BasicTextField(
                    value = text,
                    onValueChange = { onTextChange(it) },
                    modifier =
                        modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp, top = 20.dp, end = 12.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    textStyle =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                        ),
                    cursorBrush = SolidColor(Color.Gray),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text("What's happening?", color = Color.Gray, fontSize = 15.sp)
                        }
                        innerTextField()
                    },
                )
            }
            when (val currentState = state) {
                is NewPostUiState.Error ->
                    Text(
                        text = currentState.message,
                        color = Color.Red,
                    )

                NewPostUiState.Loading -> CircularProgressIndicator()
                else -> {}
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ControllerPostScreenPreview() {
    var text by remember { mutableStateOf("") }

    Scaffold { innerPading ->
        Column(Modifier.padding(innerPading)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = (Icons.Default.Close),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Button(
                    colors = ButtonDefaults.buttonColors(Green),
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {
                        TODO()
                    },
                ) { Text("Publicar", fontWeight = FontWeight.Bold) }
            }

            Row {
                AvatarItem(size = 50)
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .align(Alignment.CenterVertically)
                            .padding(start = 12.dp, top = 20.dp, end = 12.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    textStyle =
                        TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                        ),
                    cursorBrush = SolidColor(Color.Gray),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text("What's happening?", color = Color.Gray, fontSize = 15.sp)
                        }
                        innerTextField()
                    },
                )
            }
        }
    }
}
