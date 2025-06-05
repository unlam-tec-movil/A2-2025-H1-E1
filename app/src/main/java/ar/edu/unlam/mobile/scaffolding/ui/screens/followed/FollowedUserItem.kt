package ar.edu.unlam.mobile.scaffolding.ui.screens.followed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import ar.edu.unlam.mobile.scaffolding.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ar.edu.unlam.mobile.scaffolding.ui.theme.darkGreen
import ar.edu.unlam.mobile.scaffolding.ui.theme.lightGreen
import java.nio.file.WatchEvent

@Composable
fun FolloweUserItem(username : String){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ){
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color = lightGreen)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            Text(text = username)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {/*a implementar */},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = darkGreen)
        ) {
            Text(
                text = "Siguiendo",
                color = Color.White
            )
        }
    }
}