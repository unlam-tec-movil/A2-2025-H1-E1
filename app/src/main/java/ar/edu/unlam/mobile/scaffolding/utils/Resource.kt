package ar.edu.unlam.mobile.scaffolding.utils

sealed class Resource<out T> {
    class Idle<out T> : Resource<T>()

    class Loading<out T> : Resource<T>()

    data class Success<out T>(
        val data: T,
    ) : Resource<T>()

    data class Failure<out T>(
        val throwable: Throwable,
    ) : Resource<T>()
}
