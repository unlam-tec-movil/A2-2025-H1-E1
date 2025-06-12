package ar.edu.unlam.mobile.scaffolding.data.models

data class Comment(
    val idComent: Int,
    val userId: Int,
    val idPost: Int,
    val body: String,
    val likes: Int? = 0,
)
