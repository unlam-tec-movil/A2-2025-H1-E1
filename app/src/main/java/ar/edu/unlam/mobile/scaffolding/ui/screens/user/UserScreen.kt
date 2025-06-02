package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ar.edu.unlam.mobile.scaffolding.ui.components.Greeting

@Composable
fun UserScreen(userId: String) {
    Column {
        Greeting(userId)
        Text(text = "Pantalla de notificacion")
    }
}
