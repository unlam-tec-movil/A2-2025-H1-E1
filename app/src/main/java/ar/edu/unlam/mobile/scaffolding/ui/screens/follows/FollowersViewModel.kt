package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FollowersViewModel : ViewModel() {
    // lista de usuarios que te siguen
    private val _followersUsers = MutableStateFlow<List<UserProof>>(emptyList())
    val followersUsers = _followersUsers.asStateFlow()

    init {
        _followersUsers.value = List(10) {
            UserProof(
                name = "Danturron",
                username = "cacaman",
                isFollowing = false
            )
        }
    }

    fun toggleFollow(user: UserProof) {
        _followersUsers.value = _followersUsers.value.map {
            if (it.username == user.username) it.copy(isFollowing = !it.isFollowing) else it
        }
    }
}
