package ar.edu.unlam.mobile.scaffolding.utils

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {
    /**
     * Maneja errores de red y API de manera consistente
     * @param exception La excepción capturada
     * @param defaultMessage Mensaje por defecto si no se puede determinar el tipo de error
     * @return Mensaje de error descriptivo en español
     */
    fun handleNetworkError(
        exception: Exception,
        defaultMessage: String = "Error desconocido",
    ): String {
        return when (exception) {
            is UnknownHostException -> "Error de conexión. Verifica tu conexión a internet."
            is SocketTimeoutException -> "Tiempo de espera agotado. Intenta nuevamente."
            is HttpException -> {
                when (exception.code()) {
                    401 -> "No autorizado. Inicia sesión nuevamente."
                    403 -> "No tienes permisos para realizar esta acción."
                    404 -> "Recurso no encontrado."
                    500 -> "Error del servidor. Intenta más tarde."
                    502 -> "Servidor no disponible. Intenta más tarde."
                    503 -> "Servicio temporalmente no disponible."
                    else -> "Error del servidor (${exception.code()})"
                }
            }
            else -> "$defaultMessage: ${exception.message ?: "Error desconocido"}"
        }
    }

    /**
     * Maneja errores específicos para operaciones de comentarios
     */
    fun handleCommentError(exception: Exception): String {
        return handleNetworkError(exception, "Error al enviar el comentario")
    }

    /**
     * Maneja errores específicos para operaciones de posts
     */
    fun handlePostError(exception: Exception): String {
        return handleNetworkError(exception, "Error al cargar los posts")
    }

    /**
     * Maneja errores específicos para operaciones de autenticación
     */
    fun handleAuthError(exception: Exception): String {
        return handleNetworkError(exception, "Error de autenticación")
    }

    /**
     * Maneja errores específicos para operaciones de perfil
     */
    fun handleProfileError(exception: Exception): String {
        return handleNetworkError(exception, "Error al cargar el perfil")
    }
}