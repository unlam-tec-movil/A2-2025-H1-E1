package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.DarkGreen

@Composable
fun LoginScreen(navController: NavController) {
    val isImeVisible by rememberImeState()
    var rememberUser by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedHeader(isVisible = !isImeVisible)

            LoginCard(
                isImeVisible = isImeVisible,
                rememberUser = rememberUser,
                onRememberUserChange = { rememberUser = it },
                username = username,
                onUsernameChange = { username = it },
                password = password,
                onPasswordChange = { password = it },
                navController,
            )
        }
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
            modifier = Modifier.fillMaxWidth().fillMaxHeight(animatedUpperSectionRatio),
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
        Spacer(modifier = Modifier.height(30.dp))
        TitleSection()
        Spacer(modifier = Modifier.height(10.dp))

        MyTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Username",
            text = username,
            keyboardOptions = KeyboardOptions(),
            keyboardActions = KeyboardActions(),
            trailingIcon = Icons.Default.Person,
            onValueChange = onUsernameChange,
        )
        Spacer(modifier = Modifier.height(10.dp))

        MyTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "Password",
            text = password,
            keyboardOptions = KeyboardOptions(),
            keyboardActions = KeyboardActions(),
            trailingIcon = Icons.Default.Lock,
            onValueChange = onPasswordChange,
        )
        Spacer(modifier = Modifier.height(10.dp))

        RememberAndForgotRow(rememberUser = rememberUser, onCheckedChange = onRememberUserChange)

        if (isImeVisible) {
            FooterButtonSection(navController)
        } else {
            FooterButtonColumn(navController)
        }
    }
}

@Composable
fun TitleSection() {
    Text(
        text = "Welcome back!",
        fontFamily = FontFamily.Default,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = BlueGreen,
    )
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = "Login to your account",
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Remember me", color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun FooterButtonSection(navController: NavController) {
    Button(
        onClick = { navController.navigate("home") },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = BlueGreen,
                contentColor = Color.White,
            ),
        shape = RoundedCornerShape(30.dp),
    ) {
        Text("log in", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)))
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Don't you have an account?", color = DarkGreen, fontSize = 14.sp)
        TextButton(onClick = { navController.navigate("register") }) {
            Text("Sign in", color = DarkGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FooterButtonColumn(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = BlueGreen,
                    contentColor = Color.White,
                ),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text("log in", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)))
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Don't you have an account?", color = DarkGreen, fontSize = 14.sp)
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Sign in", color = DarkGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
