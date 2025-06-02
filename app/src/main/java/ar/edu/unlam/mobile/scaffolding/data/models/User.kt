package ar.edu.unlam.mobile.scaffolding.data.models

data class User(
    val userId: Int,
    val name: String,
    val avatar_url: String,
    val email: String,
    val password: String,
)
