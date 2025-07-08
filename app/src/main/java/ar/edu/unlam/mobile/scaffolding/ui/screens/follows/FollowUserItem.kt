package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import ar.edu.unlam.mobile.scaffolding.ui.components.AvatarItem

@Composable
fun FollowedUserItem(
    user: UserProof,
    onToggleFollow: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AvatarItem(avatarUrl = user.avatarUrl, size = 48)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(user.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(user.username, color = Color.Gray, fontSize = 13.sp)
            }
        }
        OutlinedButton(
            onClick = onToggleFollow,
            border = BorderStroke(1.dp, Color(0xFF2F7E6F)),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor = if (user.isFollowing) Color(0xFF2F7E6F) else Color.Gray,
                ),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text(if (user.isFollowing) "Siguiendo" else "Seguir")
        }
    }
}
