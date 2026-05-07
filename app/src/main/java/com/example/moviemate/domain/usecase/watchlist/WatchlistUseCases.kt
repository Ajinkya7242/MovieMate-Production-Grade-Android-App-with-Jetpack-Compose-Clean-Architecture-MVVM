package com.example.moviemate.domain.usecase.watchlist

import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeWatchlist()
}

class IsInWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    operator fun invoke(movieId: Int): Flow<Boolean> = repository.isInWatchlist(movieId)
}

/**
 * Toggle use case combines add+remove into one place.
 * The ViewModel just calls toggle() — it doesn't track current state.
 *
 * SOLID benefit: single source of truth for "what does toggling mean".
 * If we later add analytics on toggle, ONE place changes.
 */
class ToggleWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend operator fun invoke(movie: Movie, currentlyInList: Boolean) {
        if (currentlyInList) {
            repository.removeFromWatchlist(movie.id)
        } else {
            repository.addToWatchlist(movie)
        }
    }
}

class ClearWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    suspend operator fun invoke() = repository.clearWatchlist()
}
