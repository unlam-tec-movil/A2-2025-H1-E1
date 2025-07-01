package ar.edu.unlam.mobile.scaffolding.data.datasources.network

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseStorageService @Inject constructor() {
    // Usar el bucket explícito
    private val storage = FirebaseStorage.getInstance("gs://unlamsocial-59560.firebasestorage.app")
    private val storageRef = storage.reference

    suspend fun uploadImage(imageUri: Uri, folder: String = "avatars"): String {
        return try {
            val fileName = "${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child("$folder/$fileName")
            imageRef.putFile(imageUri).await()
            // Construir la URL
            val publicUrl = "https://firebasestorage.googleapis.com/v0/b/unlamsocial-59560.firebasestorage.app/o/${folder}%2F${fileName}?alt=media"
            publicUrl
        } catch (e: Exception) {
            throw Exception("Error al subir la imagen: ${e.message}")
        }
    }

    suspend fun deleteImage(imageUrl: String) {
        try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
        } catch (e: Exception) {
            println("Error al eliminar imagen: ${e.message}")
        }
    }
} 