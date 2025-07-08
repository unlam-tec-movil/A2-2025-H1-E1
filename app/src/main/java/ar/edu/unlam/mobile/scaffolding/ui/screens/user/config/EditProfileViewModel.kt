package ar.edu.unlam.mobile.scaffolding.ui.screens.user.config

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.FirebaseStorageService
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.ProfileResponse
import ar.edu.unlam.mobile.scaffolding.data.repositories.ProfileRespository
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
        private val firebaseStorageService: FirebaseStorageService,
    ) : ViewModel() {
        private val _user = MutableStateFlow<UserEditUiState>(UserEditUiState.Loading)
        val user: StateFlow<UserEditUiState> get() = _user

        fun loadProfile(userToken: String) {
            viewModelScope.launch {
                try {
                    _user.value = UserEditUiState.Loading
                    val profile = profileRepository.getProfile(userToken)
                    _user.value = UserEditUiState.Success(profile)
                } catch (e: Exception) {
                    val errorMsg =
                        ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handleProfileError(e)
                    _user.value = UserEditUiState.Error(errorMsg)
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
                    profileRepository.updateProfile(name, password, avatarUrl, userToken)
                    val profile = profileRepository.getProfile(userToken)
                    _user.value = UserEditUiState.Success(profile)
                } catch (e: Exception) {
                    val errorMsg =
                        ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handleProfileError(e)
                    _user.value = UserEditUiState.Error(errorMsg)
                }
            }
        }

        fun uploadAvatar(
            imageUri: Uri,
            userToken: String,
            password: String,
        ) {
            viewModelScope.launch {
                try {
                    _user.value = UserEditUiState.Uploading
                    val downloadUrl = firebaseStorageService.uploadImage(imageUri)

                    // Obtener el perfil actual para mantener los datos existentes
                    val currentProfile = profileRepository.getProfile(userToken)

                    // Actualizar solo el avatar, pero enviando la contraseña actual
                    profileRepository.updateProfile(
                        name = currentProfile.name,
                        password = password,
                        avatarUrl = downloadUrl,
                        userToken = userToken,
                    )

                    // Recargar el perfil actualizado
                    val updatedProfile = profileRepository.getProfile(userToken)
                    _user.value = UserEditUiState.Success(updatedProfile)
                } catch (e: Exception) {
                    val errorMsg = "Error al subir el avatar: ${e.message}"
                    _user.value = UserEditUiState.Error(errorMsg)
                }
            }
        }
    }

sealed interface UserEditUiState {
    object Loading : UserEditUiState

    object Uploading : UserEditUiState

    data class Success(
        val user: ProfileResponse,
    ) : UserEditUiState

    data class Error(
        val message: String,
    ) : UserEditUiState
}
