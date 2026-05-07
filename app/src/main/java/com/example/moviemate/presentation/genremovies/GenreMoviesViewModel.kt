package com.example.moviemate.presentation.genremovies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.usecase.movies.GetMoviesByGenreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Manual pagination state for genre browsing.
 *
 * Why manual pagination here instead of Paging 3:
 *   - Simpler to read for a learning project
 *   - Paging 3 is amazing but adds significant complexity (PagingSource, RemoteMediator)
 *   - We've shown the patterns; you can swap to Paging 3 later as an exercise
 */
data class GenreMoviesUiState(
    val genreName: String = "",
    val movies: List<Movie> = emptyList(),
    val isInitialLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class GenreMoviesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMoviesByGenre: GetMoviesByGenreUseCase
) : ViewModel() {

    private val genreId: Int = checkNotNull(savedStateHandle[Screen.GenreMovies.ARG_GENRE_ID])
    private val genreName: String = URLDecoder.decode(
        checkNotNull(savedStateHandle[Screen.GenreMovies.ARG_GENRE_NAME] as String?),
        "UTF-8"
    )

    private val _uiState = MutableStateFlow(GenreMoviesUiState(genreName = genreName))
    val uiState: StateFlow<GenreMoviesUiState> = _uiState.asStateFlow()

    private var currentPage = 1

    init { loadFirstPage() }

    fun loadFirstPage() {
        currentPage = 1
        _uiState.update {
            it.copy(
                movies = emptyList(),
                isInitialLoading = true,
                hasMore = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            try {
                val movies = getMoviesByGenre(genreId, 1)
                _uiState.update {
                    it.copy(
                        movies = movies,
                        isInitialLoading = false,
                        hasMore = movies.isNotEmpty()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isInitialLoading = false,
                        errorMessage = e.message ?: "Failed to load"
                    )
                }
            }
        }
    }

    /** Called when the user scrolls near the bottom of the list. */
    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMore || state.isInitialLoading) return

        _uiState.update { it.copy(isLoadingMore = true) }
        viewModelScope.launch {
            try {
                val nextPage = currentPage + 1
                val newMovies = getMoviesByGenre(genreId, nextPage)
                _uiState.update {
                    it.copy(
                        movies = it.movies + newMovies,
                        isLoadingMore = false,
                        hasMore = newMovies.isNotEmpty()
                    )
                }
                currentPage = nextPage
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false) }
            }
        }
    }
}
