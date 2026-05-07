package com.example.moviemate.presentation.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.usecase.watchlist.ClearWatchlistUseCase
import com.example.moviemate.domain.usecase.watchlist.ObserveWatchlistUseCase
import com.example.moviemate.domain.usecase.watchlist.ToggleWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Watchlist ViewModel — almost trivial because the use cases do all the work.
 *
 * The reactive Flow from the database means the UI auto-updates whenever:
 *   - User adds a movie from any other screen
 *   - User removes a movie from this screen
 *   - User clears the entire list
 *
 * No manual refresh logic needed. THIS is the power of reactive architecture.
 */
@HiltViewModel
class WatchlistViewModel @Inject constructor(
    observeWatchlist: ObserveWatchlistUseCase,
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val clearWatchlist: ClearWatchlistUseCase
) : ViewModel() {

    val watchlist: StateFlow<List<Movie>> = observeWatchlist().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun removeFromWatchlist(movie: Movie) {
        viewModelScope.launch {
            // currentlyInList = true → toggle removes it
            toggleWatchlist(movie, currentlyInList = true)
        }
    }

    fun clearAll() {
        viewModelScope.launch { clearWatchlist() }
    }
}
