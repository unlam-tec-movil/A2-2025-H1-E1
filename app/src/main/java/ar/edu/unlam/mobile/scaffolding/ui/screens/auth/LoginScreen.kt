package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile.scaffolding.ui.theme.blueGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.darkGreen

@Preview
@Composable
fun LoginScreen() {
    val isImeVisible by rememberImeState()
    var rememberUser by remember { mutableStateOf(false) }

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
            val animatedUpperSectionRatio by animateFloatAsState(
                targetValue = if (isImeVisible) 0f else 0.35f,
                label = "",
            )
            AnimatedVisibility(visible = !isImeVisible, enter = fadeIn(), exit = fadeOut()) {
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
                        // Esto hace que la imagen llene el espacio
                        modifier = Modifier.matchParentSize(),
                    )

                    Image(
                        painter = painterResource(id = ar.edu.unlam.mobile.scaffolding.R.drawable.unlamlogo),
                        contentDescription = "logo",
                        modifier = Modifier.size(100.dp),
                    )
                }
            }

            // Columna blanca con los bordes redondeados
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "Welcome back!",
                    fontFamily = FontFamily.Default,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueGreen,
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Login to your account",
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

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Grupo izquierdo: Checkbox + "Remember me"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberUser,
                            onCheckedChange = { rememberUser = it },
                            colors =
                                CheckboxDefaults.colors(
                                    checkedColor = blueGreen,
                                    uncheckedColor = Color.Gray,
                                ),
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Remember me",
                            color = Color.Black,
                            fontSize = 14.sp,
                        )
                    }

                    // Botón a la derecha
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
                        onClick = { /* TODO */ },
                    ) {
                        Text("Forgot your password?", color = darkGreen, fontSize = 14.sp)
                    }
                }

                if (isImeVisible) {
                    Button(
                        onClick = { /*TODO*/ },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 60.dp)
                                .padding(horizontal = 16.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = blueGreen,
                                contentColor = Color.White,
                            ),
                        shape = RoundedCornerShape(30.dp),
                    ) {
                        Text(
                            text = "log in",
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
                            text = "Don't you have an account?",
                            color = darkGreen,
                            fontSize = 14.sp,
                        )

                        TextButton(onClick = { /* TODO */ }) {
                            Text("Sign in", color = darkGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = blueGreen,
                                    contentColor = Color.White,
                                ),
                            // enabled = inputValido,
                            shape = RoundedCornerShape(30.dp),
                        ) {
                            Text(
                                text = "log in",
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
                                text = "Don't you have an account?",
                                color = darkGreen,
                                fontSize = 14.sp,
                            )

                            TextButton(onClick = { /* TODO */ }) {
                                Text("Sign in", color = darkGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        // Spacer(modifier = Modifier.height(200.dp))
                    }
                }
            }
        }
    }
}
