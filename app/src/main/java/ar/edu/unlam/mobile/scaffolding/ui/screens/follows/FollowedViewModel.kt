package ar.edu.unlam.mobile.scaffolding.ui.screens.follows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.models.UserProof
import ar.edu.unlam.mobile.scaffolding.data.repositories.FollowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowedViewModel
    @Inject
    constructor(
        private val followRepository: FollowRepository,
    ) : ViewModel() {
        private val _followedUsers = MutableStateFlow<List<UserProof>>(emptyList())
        val followedUsers: StateFlow<List<UserProof>> = _followedUsers.asStateFlow()

        private var currentUserEmail: String = ""

        fun loadFollowedUsers(userEmail: String) {
            currentUserEmail = userEmail
            viewModelScope.launch {
                try {
                    val users = followRepository.getFollowedUsers(userEmail)
                    _followedUsers.value = users
                } catch (e: Exception) {
                    // En caso de error, mantener la lista vacía
                    _followedUsers.value = emptyList()
                }
            }
        }

        fun toggleFollow(user: UserProof) {
            viewModelScope.launch {
                try {
                    if (user.isFollowing) {
                        followRepository.unfollowUser(currentUserEmail, user.username)
                    } else {
                        followRepository.followUser(currentUserEmail, user.username, user.name, "")
                    }
                    // Recargar la lista después de cambiar el estado
                    loadFollowedUsers(currentUserEmail)
                } catch (e: Exception) {
                    // Manejar el error si es necesario
                }
            }
        }
    }
