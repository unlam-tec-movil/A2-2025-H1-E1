package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserViewModel
import ar.edu.unlam.mobile.scaffolding.ui.theme.Green
import ar.edu.unlam.mobile.scaffolding.utils.UserStore

@Composable
fun TopBar(
    title: String = "",
    onActionClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel(),
) {
    var showLogoutModal by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userStore = remember { UserStore(context) }
    val tokenState = userStore.leerTokenUsuario.collectAsState(initial = "")
    val token = tokenState.value
    val userState by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(token) {
        if (token.isNotEmpty()) {
            viewModel.loadProfile(token)
        }
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Green)
                .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(R.drawable.unlamlogo),
            contentDescription = "User_image",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
        )

        Text(
            text = title,
            maxLines = 1,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
        )

        IconButton(
            onClick = {
                showLogoutModal = true
                onActionClick()
            },
        ) {
            when (val state = userState) {
                is UserUiState.Loading -> {
                    AvatarItem(size = 50)
                }

                is UserUiState.Success -> {
                    AvatarItem(avatarUrl = state.user.avatarUrl, size = 50)
                }

                is UserUiState.Error -> {
                    AvatarItem(size = 50)
                }
            }
        }
    }

    // LogoutModal
    if (showLogoutModal) {
        LogoutModal(
            onDismiss = { showLogoutModal = false },
            onConfirmLogout = {
                showLogoutModal = false
                onLogout()
            },
        )
    }
}
