package ar.edu.unlam.mobile.scaffolding.ui.screens.post.draft

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ar.edu.unlam.mobile.scaffolding.data.datasources.local.entities.DraftEntity
import ar.edu.unlam.mobile.scaffolding.ui.components.DraftItem

// TODO: Pantalla para ver y gestionar borradores.
fun draft() {}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraftContainerScreen(
    onNavigateBack: () -> Unit,
    onEditDraft: (DraftEntity) -> Unit,
    viewModel: DraftContainerViewModel = hiltViewModel(),
) {
    val draftsState = viewModel.drafts.collectAsStateWithLifecycle()
    val isLoadingState = viewModel.isLoading.collectAsStateWithLifecycle()
    val errorState = viewModel.error.collectAsStateWithLifecycle()
    val drafts = draftsState.value
    val isLoading = isLoadingState.value
    val error = errorState.value

    // TODO: Obtener email del usuario desde DataStore
    LaunchedEffect(Unit) {
        viewModel.loadDrafts("alexis")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Borradores",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                error != null -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = error ?: "Error desconocido",
                            color = Color.Red,
                            fontSize = 16.sp,
                        )
                    }
                }
                drafts.isEmpty() -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "No tienes borradores guardados",
                            fontSize = 18.sp,
                            color = Color.Gray,
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    ) {
                        items(drafts) { draft ->
                            DraftItem(
                                draft = draft,
                                onEditClick = { onEditDraft(draft) },
                                onDeleteClick = { viewModel.deleteDraft(draft) },
                            )
                        }
                    }
                }
            }
        }
    }
}
