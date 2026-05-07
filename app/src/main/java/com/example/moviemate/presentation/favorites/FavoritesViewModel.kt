package com.example.moviemate.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.usecase.favorites.ClearFavoritesUseCase
import com.example.moviemate.domain.usecase.favorites.ObserveFavoritesUseCase
import com.example.moviemate.domain.usecase.favorites.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavorites: ObserveFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val clearFavorites: ClearFavoritesUseCase
) : ViewModel() {

    val favorites: StateFlow<List<Movie>> = observeFavorites().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun removeFromFavorites(movie: Movie) {
        viewModelScope.launch { toggleFavorite(movie, currentlyFavorite = true) }
    }

    fun clearAll() {
        viewModelScope.launch { clearFavorites() }
    }
}
