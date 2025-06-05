package ar.edu.unlam.mobile.scaffolding.ui.screens.followed

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FollowedViewModel : ViewModel(){
    private val _followedUsers = mutableStateListOf<String>()
    val followedUsers: List<String> get() = _followedUsers

    init {
        _followedUsers.addAll(
            listOf("Yaircito24cm", "ElDante", "Proweb777")
        )
    }

}