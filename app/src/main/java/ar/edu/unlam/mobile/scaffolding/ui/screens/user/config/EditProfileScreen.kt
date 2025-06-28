package ar.edu.unlam.mobile.scaffolding.ui.screens.user.config

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R

@Preview
@Composable
fun Edit(controller: NavHostController = rememberNavController()) {
    //  var username by remember { mutableStateOf("@Hola4576") }
    //  var name by remember { mutableStateOf("Tungtung Sahur") }
    //  var bio by remember { mutableStateOf("Aadsbsa sadhga ed ahdfba chenbfsb ahvharbgrh ansbdbdhff hdbfc b fdvnbajhrew e regbgrqwgrr fgjkaerjnuoj") }

    // Fondo superior verde
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .background(Color.White),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            // botón de cerrar
            IconButton(
                onClick =
                    { },
                modifier =
                    Modifier
                        .align(Alignment.TopStart),
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                )
            }

            // guardar
            Button(
                onClick = {},
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

        // Banner verde con icono de cámara

        // Foto de perfil

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
                IconButton(onClick = {}) {
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
                            .clickable(onClick = { /* Acción */ }),
                )
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Cambiar banner",
                    tint = Color.White,
                    modifier =
                        Modifier.size(35.dp)
                            .offset(y = 80.dp),
                )
            }

            // Foto de perfil redonda (superpuesta)

            Spacer(modifier = Modifier.height(32.dp))
        }

        Spacer(modifier = Modifier.height(80.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
        ) {
            listOf(
                "Usuario" to "@Hola4576",
                "Nombre" to "Tungtung Sahur",
                "Biografía" to "Aadsbsa sadhga ed ahdfba chenbfsb ahvharbgrh ansbdbdhff hdbfc b fdvnbajhrew e regbgrqwgr fgjkaerjnuoj.",
            ).forEach { (titulo, valor) ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                    )
                    TextField(
                        value = valor,
                        onValueChange = {},
                        readOnly = true,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(if (titulo == "Biografía") 100.dp else 56.dp),
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
                }
            }
        }
    }
}
