package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof

@Composable
fun FollowedContent(
    users: List<UserProof>,
    onToggleFollow: (UserProof) -> Unit,
    title: String = "Siguiendo",
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(text = title, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
            items(users) { user ->
                FollowedUserItem(
                    user = user,
                    onToggleFollow = { onToggleFollow(user) },
                )
            }
        }
    }
}

@Composable
fun Followed(
    viewModel: FollowedViewModel = hiltViewModel(),
    currentUserEmail: String = "",
) {
    val users by viewModel.followedUsers.collectAsState()

    LaunchedEffect(currentUserEmail) {
        if (currentUserEmail.isNotEmpty()) {
            viewModel.loadFollowedUsers(currentUserEmail)
        }
    }

    FollowedContent(
        users = users,
        onToggleFollow = { viewModel.toggleFollow(it) },
        title = "Seguidores",
    )
}

@Composable
fun Followers(viewModel: FollowersViewModel = viewModel()) {
    val users by viewModel.followersUsers.collectAsState()
    FollowedContent(
        users = users,
        onToggleFollow = { viewModel.toggleFollow(it) },
        title = "Seguidores",
    )
}

@Preview(showBackground = true)
@Composable
fun FollowedContentPreview() {
    Followed()
}

@Preview(showBackground = true)
@Composable
fun FollowersontentPreview() {
    Followers()
}
