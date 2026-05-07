package com.example.moviemate.domain.repository

import com.example.moviemate.domain.model.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for the user's WATCHLIST — movies they want to watch later.
 *
 * Reactive design with Flow:
 *   - Returning Flow means the UI auto-updates whenever the underlying data changes
 *   - No need to manually refresh after adding/removing items
 *   - This is the modern reactive pattern recommended by Google
 */
interface WatchlistRepository {
    /** Reactive stream of all watchlist movies, newest first. */
    fun observeWatchlist(): Flow<List<Movie>>

    /** Check whether a specific movie is already in the watchlist. */
    fun isInWatchlist(movieId: Int): Flow<Boolean>

    suspend fun addToWatchlist(movie: Movie)
    suspend fun removeFromWatchlist(movieId: Int)
    suspend fun clearWatchlist()
}

/**
 * Repository contract for FAVORITES — movies the user loved.
 * Separate from watchlist so users can mark "want to watch" vs "loved this".
 */
interface FavoritesRepository {
    fun observeFavorites(): Flow<List<Movie>>
    fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun addToFavorites(movie: Movie)
    suspend fun removeFromFavorites(movieId: Int)
    suspend fun clearFavorites()
}
