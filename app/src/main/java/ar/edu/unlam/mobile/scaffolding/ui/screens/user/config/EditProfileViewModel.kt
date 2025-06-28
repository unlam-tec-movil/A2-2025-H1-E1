package ar.edu.unlam.mobile.scaffolding.ui.screens.user.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
import ar.edu.unlam.mobile.scaffolding.ui.screens.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel
    @Inject
    constructor(
        private val profileRepository: ProfileRespository,
    ) : ViewModel() {
        private val _user = MutableStateFlow<UserUiState>(UserUiState.Loading)
        val user: StateFlow<UserUiState> get() = _user

        fun loadProfile(userToken: String) {
            viewModelScope.launch {
                try {
                    _user.value = UserUiState.Loading
                    val profile = profileRepository.getProfile(userToken)
                    _user.value = UserUiState.Success(profile)
                } catch (e: Exception) {
                    _user.value = UserUiState.Error(e.message ?: "Error desconocido")
                }
            }
        }

        fun updateUser(
            name: String,
            password: String,
            avatarUrl: String,
            userToken: String,
        ) {
            viewModelScope.launch {
                try {
                    profileRepository.updateProfile(name, password, avatarUrl)
                    // Reload profile after update
                    val profile = profileRepository.getProfile(userToken)
                    _user.value = UserUiState.Success(profile)
                } catch (e: Exception) {
                    _user.value = UserUiState.Error(e.message ?: "Error desconocido")
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
    }
