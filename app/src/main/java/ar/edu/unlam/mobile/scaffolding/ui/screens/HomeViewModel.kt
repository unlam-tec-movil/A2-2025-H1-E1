package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Immutable
sealed interface HelloMessageUIState {
    data class Success(
        val message: String,
    ) : HelloMessageUIState

    data object Loading : HelloMessageUIState

    data class Error(
        val message: String,
    ) : HelloMessageUIState
}

data class HomeUIState(
    val helloMessageState: HelloMessageUIState,
)

@Parcelize
data class PostUi(
    val id: String,
    val title: String,
    val image: String?,
    val isFavorite: Boolean
) : Parcelable

@HiltViewModel
class HomeViewModel
@Inject
constructor() : ViewModel() {
    private val helloMessage = MutableStateFlow<HelloMessageUIState>(HelloMessageUIState.Loading)
    private val _uiState = MutableStateFlow(HomeUIState(helloMessage.value))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = HomeUIState(HelloMessageUIState.Success("2b"))
    }
}

