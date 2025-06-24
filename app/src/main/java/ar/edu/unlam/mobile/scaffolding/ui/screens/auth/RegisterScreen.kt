package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.DarkGreen

@Composable
fun RegisterScreenK(navController: NavHostController) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Register",
                fontFamily = FontFamily.Default,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = BlueGreen,
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create your new account",
                fontFamily = FontFamily.Default,
                fontSize = 15.sp,
                color = Color.Gray,
            )
            Spacer(modifier = Modifier.height(30.dp))

            var username by remember { mutableStateOf("Username") }

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "",
                text = "Username",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Person,
                onValueChange = { username = it },
            )

            Spacer(modifier = Modifier.height(20.dp))

            var email by remember { mutableStateOf("E-mail") }

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "",
                text = "E-mail",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.MailOutline,
                onValueChange = { email = it },
            )
            Spacer(modifier = Modifier.height(10.dp))
            var password by remember { mutableStateOf("Password") }
            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "",
                text = "Password",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Lock,
                onValueChange = { password = it },
            )

            Spacer(modifier = Modifier.height(10.dp))

            var confirmPassword by remember { mutableStateOf("PasswordConfirm") }

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "",
                text = "Confirm password",
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Lock,
                onValueChange = { confirmPassword = it },
            )
            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = { /* TODO */ },
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .width(300.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = BlueGreen,
                        contentColor = Color.White,
                    ),
                shape = RoundedCornerShape(30.dp),
            ) {
                Text(
                    text = "Sign up",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)),
                )
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Already have an account?",
                    color = DarkGreen,
                    fontSize = 14.sp,
                )

                TextButton(onClick = { /* TODO */ }) {
                    Text("Login", color = DarkGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            RegisterTitleSection()

            Spacer(modifier = Modifier.height(30.dp))

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "Username",
                text = username,
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Person,
                onValueChange = { username = it },
            )

            Spacer(modifier = Modifier.height(10.dp))

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "E-mail",
                text = email,
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.MailOutline,
                onValueChange = { email = it },
            )

            Spacer(modifier = Modifier.height(10.dp))

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "Password",
                text = password,
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Lock,
                onValueChange = { password = it },
            )

            Spacer(modifier = Modifier.height(10.dp))

            MyTextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "PasswordConfirm",
                text = confirmPassword,
                keyboardOptions = KeyboardOptions(),
                keyboardActions = KeyboardActions(),
                trailingIcon = Icons.Default.Lock,
                onValueChange = { confirmPassword = it },
            )

            Spacer(modifier = Modifier.height(60.dp))

            RegisterButton(navController)

            AlreadyHaveAccountSection(navController)
        }
    }
}

@Composable
fun RegisterTitleSection() {
    Text(
        text = "Register",
        fontFamily = FontFamily.Default,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = BlueGreen,
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Create your new account",
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        color = Color.Gray,
    )
}

@Composable
fun RegisterButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("login") },
        modifier = Modifier.width(300.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = BlueGreen,
                contentColor = Color.White,
            ),
        shape = RoundedCornerShape(30.dp),
    ) {
        Text(
            text = "Sign up",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight(500)),
        )
    }
}

@Composable
fun AlreadyHaveAccountSection(navController: NavHostController) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Already have an account?",
            color = DarkGreen,
            fontSize = 14.sp,
        )
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Login", color = DarkGreen, fontWeight = FontWeight.Bold)
        }
    }
}
