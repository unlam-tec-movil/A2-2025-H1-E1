package ar.edu.unlam.mobile.scaffolding.data.models

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val urlPostImage: String? = null,
    val likes: Int? = 0,
    val comments: Int? = 0,
)
