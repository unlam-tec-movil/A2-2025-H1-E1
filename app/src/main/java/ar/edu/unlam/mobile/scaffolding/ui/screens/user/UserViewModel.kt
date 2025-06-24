package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.api.ProfileApiService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.requests.ProfileRequest
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.Tuit
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
import ar.edu.unlam.mobile.scaffolding.ui.screens.feed.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel
@Inject
constructor(
    private val profileRepository: ProfileRespository,
) : ViewModel() {

    private val _user = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val user : StateFlow<UserUiState> get() = _user

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            try {
                _user.value = UserUiState.Success(profileRepository.getProfile())
            } catch (e: Exception) {
                _user.value = UserUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed interface UserUiState {
    object Loading : UserUiState

    data class Success(
        val user: ProfileResponse,
    ) : UserUiState

    data class Error(
        val message: String,
    ) : UserUiState
}