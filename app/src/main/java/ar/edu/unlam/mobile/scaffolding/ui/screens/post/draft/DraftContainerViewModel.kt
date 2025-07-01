package ar.edu.unlam.mobile.scaffolding.ui.screens.post.draft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.repository.DraftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DraftContainerViewModel
    @Inject
    constructor(
        private val draftRepository: DraftRepository,
    ) : ViewModel() {
        private val _drafts = MutableStateFlow<List<DraftEntity>>(emptyList())
        val drafts: StateFlow<List<DraftEntity>> = _drafts

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        fun loadDrafts(userEmail: String) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val draftsList = draftRepository.getDraftsByUser(userEmail)
                    _drafts.value = draftsList
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = "Error al cargar borradores: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun deleteDraft(draft: DraftEntity) {
            viewModelScope.launch {
                try {
                    draftRepository.deleteDraft(draft)
                    // Recargar la lista después de eliminar
                    loadDrafts(draft.userEmail)
                } catch (e: Exception) {
                    _error.value = "Error al eliminar borrador: ${e.message}"
                }
            }
        }
    }
