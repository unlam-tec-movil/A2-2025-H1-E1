package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.LoginResponse
import ar.edu.unlam.mobile.scaffolding.data.datasources.network.responses.UserResponse
import ar.edu.unlam.mobile.scaffolding.data.repositories.AuthRepository
import ar.edu.unlam.mobile.scaffolding.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Idle())
        val loginState: StateFlow<Resource<LoginResponse>> = _loginState

        fun login(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _loginState.value = Resource.Loading()

                try {
                    val response = authRepository.login(email, password)
                    _loginState.value = Resource.Success(response)
                } catch (e: Exception) {
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handleAuthError(e)
                    _loginState.value = Resource.Failure(Exception(errorMsg))
                }
            }
        }
    }

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        private val _registerState = MutableStateFlow<Resource<UserResponse>>(Resource.Idle())
        val registerState: StateFlow<Resource<UserResponse>> = _registerState

        fun registerUser(
            name: String,
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _registerState.value = Resource.Loading()
                try {
                    val response = authRepository.users(name, email, password)
                    _registerState.value = Resource.Success(response)
                } catch (e: Exception) {
                    val errorMsg = ar.edu.unlam.mobile.scaffolding.utils.ErrorHandler.handleAuthError(e)
                    _registerState.value = Resource.Failure(Exception(errorMsg))
                }
            }
        }
    }
