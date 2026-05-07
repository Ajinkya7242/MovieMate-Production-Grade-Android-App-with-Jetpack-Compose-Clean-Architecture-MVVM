package com.example.moviemate.core.utils

/**
 * A generic wrapper for representing the state of any operation that involves
 * loading data (e.g., network calls, database queries).
 *
 * This is a sealed class because there's a fixed, known set of states.
 * The compiler can then verify that we handle all cases in `when` expressions.
 *
 * Why we use this pattern (Single Responsibility from SOLID):
 *   - Each state has ONE clear meaning
 *   - UI layer doesn't need to know HOW data was fetched, only its current state
 *   - Easy to test - you can return any state in unit tests
 *
 * Usage example in ViewModel:
 *   _uiState.update { Resource.Loading }
 *   try { _uiState.update { Resource.Success(repo.getMovies()) } }
 *   catch (e: Exception) { _uiState.update { Resource.Error(e.message) } }
 */
sealed class Resource<out T> {

    /** Operation succeeded with data of type T. */
    data class Success<T>(val data: T) : Resource<T>()

    /**
     * Operation failed.
     * @param message Human-readable error message safe to show to users
     * @param throwable Underlying exception for logging (optional)
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : Resource<Nothing>()

    /** Operation is in progress. */
    data object Loading : Resource<Nothing>()
}

/**
 * Helper extension to map a Resource of one type to another.
 * Useful for converting domain models in ViewModels.
 */
inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> this
    is Resource.Loading -> this
}
