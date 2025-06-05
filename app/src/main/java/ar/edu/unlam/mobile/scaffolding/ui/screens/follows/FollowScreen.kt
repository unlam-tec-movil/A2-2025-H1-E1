package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import androidx.compose.runtime.getValue

@Composable
fun FollowedContent(
    users: List<UserProof>,
    onToggleFollow: (UserProof) -> Unit,
    title: String = "Siguiendo"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = title, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
            items(users) { user ->
                FollowedUserItem(
                    user = user,
                    onToggleFollow = { onToggleFollow(user) }
                )
            }
        }
    }
}

@Composable
fun Followed(viewModel: FollowedViewModel = viewModel()) {
    val users by viewModel.followedUsers.collectAsState()
    FollowedContent(
        users = users,
        onToggleFollow = { viewModel.toggleFollow(it) },
        title = "Siguiendo"
    )
}

@Composable
fun Followers(viewModel: FollowersViewModel = viewModel()) {
    val users by viewModel.followersUsers.collectAsState()
    FollowedContent(
        users = users,
        onToggleFollow = { viewModel.toggleFollow(it) },
        title = "Seguidores"
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