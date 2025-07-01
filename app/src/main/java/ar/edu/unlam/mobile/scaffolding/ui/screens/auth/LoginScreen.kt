package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.DarkGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.GrayLight
import ar.edu.unlam.mobile.scaffolding.utils.Resource
import ar.edu.unlam.mobile.scaffolding.utils.UserStore
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val isImeVisible by rememberImeState()

    var rememberUser by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val savedUserState = userStore.leerDatosUsuario.collectAsState(initial = "")
    val savedUser = savedUserState.value
    val estaLogueadoState = userStore.leerEstadoLogin.collectAsState(initial = false)
    val estaLogueado = estaLogueadoState.value

    LaunchedEffect(estaLogueado) {
        if (estaLogueado) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LaunchedEffect(savedUser) {
        if (savedUser.isNotEmpty()) {
            username = savedUser
            rememberUser = true
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedHeader(isVisible = !isImeVisible)

            LoginCard(
                isImeVisible = isImeVisible,
                rememberUser = rememberUser,
                onRememberUserChange = { checked ->
                    rememberUser = checked
                    coroutineScope.launch {
                        if (!checked) {
                            coroutineScope.launch {
                                userStore.escribirDatosUsuario("")
                                userStore.escribirEstadoLogin(false)
                            }
                        }
                    }
                },
                username = username,
                onUsernameChange = { username = it },
                password = password,
                onPasswordChange = { password = it },
                onLoginClick = {
                    viewModel.login(username, password)
                },
                navController = navController,
            )
        }
    }

    // Estadito de carga o error. TODO: Usar un toast personalizado
    when (loginState) {
//        is Resource.Loading -> {
//            CircularProgressIndicator()
//        }

        is Resource.Success -> {
            LaunchedEffect(Unit) {
                val token = (loginState as Resource.Success).data.token
                coroutineScope.launch {
                    userStore.escribirTokenUsuario(token)
                }
                if (rememberUser) {
                    userStore.escribirDatosUsuario(username)
                    userStore.escribirEstadoLogin(true)
                } else {
                    userStore.escribirDatosUsuario("")
                    userStore.escribirEstadoLogin(false)
                }

                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        is Resource.Failure -> {
            LaunchedEffect(loginState) {
                val error = (loginState as Resource.Failure).throwable.message
                Toast
                    .makeText(
                        context,
                        error ?: "Error de autenticación",
                        Toast.LENGTH_SHORT,
                    ).show()
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun AnimatedHeader(isVisible: Boolean) {
    val animatedUpperSectionRatio by animateFloatAsState(
        targetValue = if (isVisible) 0.35f else 0f,
        label = "",
    )

    AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(animatedUpperSectionRatio),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = ar.edu.unlam.mobile.scaffolding.R.drawable.unlamoptionb),
                contentDescription = "Fondo superior",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
            Image(
                painter = painterResource(id = ar.edu.unlam.mobile.scaffolding.R.drawable.unlamlogo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
            )
        }
    }
}

@Composable
fun LoginCard(
    isImeVisible: Boolean,
    rememberUser: Boolean,
    onRememberUserChange: (Boolean) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    navController: NavController,
) {


    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 20.dp))
                .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        TitleSection()
        Spacer(modifier = Modifier.height(10.dp))

        MyTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "E-mail",
            text = username,
            keyboardOptions = KeyboardOptions(),
            keyboardActions = KeyboardActions(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "E-mail",
                    tint = Color.Gray
                )
            },
            onValueChange = onUsernameChange,
        )

        Spacer(modifier = Modifier.height(10.dp))

        var isVisible by rememberSaveable { mutableStateOf(false) }
        MyTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Contraseña",
            text = password,
            keyboardOptions = KeyboardOptions(),
            keyboardActions = KeyboardActions(),
            trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {

                    Icon(
                        imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color.Gray
                    )
                }
            },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            contentDescription = "Password",
            onValueChange = onPasswordChange,
        )
        Spacer(modifier = Modifier.height(10.dp))

        RememberAndForgotRow(rememberUser = rememberUser, onCheckedChange = onRememberUserChange)

        if (isImeVisible) {
            FooterButtonSection(onLoginClick = onLoginClick, navController)
        } else {
            FooterButtonColumn(onLoginClick = onLoginClick, navController)
        }
    }
}

@Composable
fun TitleSection() {
    Text(
        text = "¡Bienvenido!",
        fontFamily = FontFamily.Default,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = BlueGreen,
    )
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = "Ingresa a tu cuenta",
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        color = Color.Gray,
    )
}

@Composable
fun RememberAndForgotRow(
    rememberUser: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = rememberUser,
                onCheckedChange = onCheckedChange,
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = BlueGreen,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Recuérdame", color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun FooterButtonSection(
    onLoginClick: () -> Unit,
    navController: NavController,
) {
    Button(
        onClick = { onLoginClick() },
        modifier =
            Modifier.width(300.dp),
        //   .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = BlueGreen,
                contentColor = Color.White,
            ),
        shape = RoundedCornerShape(30.dp),
    ) {
        Text("Iniciar sesión", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)))
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "¿No tienes una cuenta?", color = DarkGreen, fontSize = 14.sp)
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Regístrate", color = DarkGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FooterButtonColumn(
    onLoginClick: () -> Unit,
    navController: NavController,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.width(300.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = BlueGreen,
                    contentColor = Color.White,
                ),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text(
                "Iniciar sesión",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)),
            )
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "¿No tienes una cuenta?", color = DarkGreen, fontSize = 14.sp)
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Regístrate", color = DarkGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
