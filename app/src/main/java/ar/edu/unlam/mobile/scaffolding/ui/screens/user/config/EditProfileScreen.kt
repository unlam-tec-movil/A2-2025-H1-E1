package ar.edu.unlam.mobile.scaffolding.ui.screens.user.config

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import coil.compose.rememberAsyncImagePainter

@Composable
fun Edit(
    controller: NavHostController = rememberNavController(),
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userState by viewModel.user.collectAsStateWithLifecycle()
    var name = ""

    when (val state = userState) {
        is UserEditUiState.Success -> {
            val user = state.user
             name = user.name
            val avatarUrl = user.avatarUrl
        }
        is UserEditUiState.Error ->{
        }
        UserEditUiState.Loading -> {}

    }

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)

        }
    }

    var bio by remember {
        mutableStateOf(
            "Aadsbsa sadhga ed ahdfba chenbfsb ahvharbgrh ansbdbdhff hdbfc b fdvnbajhrew e regbgrqwgrr fgjkaerjnuoj",
        )
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri ->
            imageUri = uri
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.White),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            IconButton(
                onClick = { controller.navigate("user/{id}") },
                modifier = Modifier.align(Alignment.TopStart),
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                )
            }

            Button(
                onClick = {
                    viewModel.updateUser(name, "", imageUri?.toString() ?: "", token)
                    controller.navigate("user/{id}")
                },
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386A5F)),
            ) {
                Text("Guardar", color = Color.White)
            }
        }

        Box(
            modifier =
                Modifier
                    .offset(y = (-0).dp)
                    .align(Alignment.CenterHorizontally),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFF386A5F)),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Cambiar banner",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp),
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.profile_photo),
                    contentDescription = "Profile photo",
                    modifier =
                        Modifier
                            .size(95.dp)
                            .offset(y = 80.dp)
                            .clip(CircleShape)
                            .border(0.1.dp, Color.White, CircleShape)
                            .clickable(onClick = { launcher.launch("image/*") }),
                )

              /*  imageUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Imagen seleccionada",
                        modifier =
                            Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                    )
                }
                */
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Cambiar banner",
                    tint = Color.White,
                    modifier =
                        Modifier
                            .size(35.dp)
                            .offset(y = 80.dp),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Spacer(modifier = Modifier.height(80.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            when (val state = userState) {
                is UserEditUiState.Success -> {
                    val user = state.user
                    name = user.name
                }

                    is UserEditUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    }

                is UserEditUiState.Error -> {}
            }


            Text(
                text = "Usuario",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color(0xFF386A5F),
                        disabledTextColor = LocalContentColor.current,
                        disabledLabelColor = Color.Gray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
            )

            Text(
                text = "Nombre",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color(0xFF386A5F),
                        disabledTextColor = LocalContentColor.current,
                        disabledLabelColor = Color.Gray,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray,
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = Color.White,
                    ),
            )

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Biografía",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                )
                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                    colors =
                        TextFieldDefaults.colors(
                            unfocusedTextColor = Color(0xFF386A5F),
                            disabledTextColor = LocalContentColor.current,
                            disabledLabelColor = Color.Gray,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray,
                            unfocusedContainerColor = Color.White,
                            focusedLabelColor = Color.White,
                        ),
                )
            }
        }
    }
}

