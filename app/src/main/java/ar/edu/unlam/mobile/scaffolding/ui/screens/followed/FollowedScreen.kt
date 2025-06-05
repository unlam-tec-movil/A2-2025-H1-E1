package ar.edu.unlam.mobile.scaffolding.ui.screens.followed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun followed(viewModel: FollowedViewModel = viewModel()){
    val followedUsers = viewModel.followedUsers

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text(
            text = "Siguiendo",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(followedUsers) { user ->
                  FolloweUserItem(username = user)
            }
        }
    }
}

@Preview
@Composable
fun prueba(){
    followed()
}