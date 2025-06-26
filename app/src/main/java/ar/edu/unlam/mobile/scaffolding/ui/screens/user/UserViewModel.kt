package ar.edu.unlam.mobile.scaffolding.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
    @Inject
    constructor(
        private val profileRepository: ProfileRespository,
    ) : ViewModel() {
        private val _profileState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
        val profileState: StateFlow<ProfileUiState> = _profileState

        fun loadProfile(userToken: String) {
            viewModelScope.launch {
                try {
                    _profileState.value = ProfileUiState.Loading
                    val profile = profileRepository.getProfile(userToken)
                    _profileState.value = ProfileUiState.Success(profile)
                } catch (e: Exception) {
                    _profileState.value = ProfileUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }

sealed interface ProfileUiState {
    object Loading : ProfileUiState

    data class Success(
        val profile: ProfileResponse,
    ) : ProfileUiState

    data class Error(
        val message: String,
    ) : ProfileUiState
}
