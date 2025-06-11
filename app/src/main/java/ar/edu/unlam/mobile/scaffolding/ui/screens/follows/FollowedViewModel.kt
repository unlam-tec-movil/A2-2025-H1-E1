package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FollowedViewModel : ViewModel() {
    // Simulación de datos de usuarios seguidos
    private val _followedUsers = MutableStateFlow<List<UserProof>>(emptyList())
    val followedUsers: StateFlow<List<UserProof>> = _followedUsers.asStateFlow()

    init {
        // Cargar usuarios
        loadFollowedUsers()
    }

    private fun loadFollowedUsers() {
        _followedUsers.value =
            List(10) {
                UserProof(
                    name = "Yairciño",
                    username = "Yairciño24",
                    isFollowing = true,
                )
            }
    }

    fun toggleFollow(user: UserProof) {
        _followedUsers.value =
            _followedUsers.value.map {
                if (it.username == user.username) it.copy(isFollowing = !it.isFollowing) else it
            }
    }
}
