package ar.edu.unlam.mobile.scaffolding.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStore(
    private val context: Context,
) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datos")
        val DATOS_USUARIO = stringPreferencesKey("datos_usuario")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    val leerDatosUsuario: Flow<String> =
        context.dataStore.data
            .map {
                it[DATOS_USUARIO] ?: ""
            }

    val leerEstadoLogin: Flow<Boolean> =
        context.dataStore.data
            .map { it[IS_LOGGED_IN] ?: false }


    val leerTokenUsuario: Flow<String> =
        context.dataStore.data
            .map { it[USER_TOKEN] ?: "" }


    suspend fun escribirDatosUsuario(datosUsuario: String) {
        context.dataStore.edit {
            it[DATOS_USUARIO] = datosUsuario
        }
    }

    suspend fun escribirEstadoLogin(loggedIn: Boolean) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun escribirTokenUsuario(token: String) {
        context.dataStore.edit {
            it[USER_TOKEN] = token
        }
    }
}
