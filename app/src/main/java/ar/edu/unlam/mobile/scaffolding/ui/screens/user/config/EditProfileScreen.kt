package ar.edu.unlam.mobile.scaffolding.ui.screens.user.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TODO: Pantalla para editar perfil.

@Preview
@Composable
 fun edit() {
 Column(){

  Box(modifier = Modifier
   .fillMaxWidth()
   .height(50.dp)
   .background(Color(0xFF4B877A))

  )

   Box(modifier = Modifier.
   fillMaxWidth()
   .height(150.dp)
   .background(Color(0xFF4B877A)))



 }


}
