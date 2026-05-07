package com.example.moviemate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.utils.AppException
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.usecase.movies.GetNowPlayingMoviesUseCase
import com.example.moviemate.domain.usecase.movies.GetPopularMoviesUseCase
import com.example.moviemate.domain.usecase.movies.GetTopRatedMoviesUseCase
import com.example.moviemate.domain.usecase.movies.GetTrendingMoviesUseCase
import com.example.moviemate.domain.usecase.movies.GetUpcomingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for the Home screen.
 *
 * Why a single immutable data class:
 *   - One source of truth for the entire screen
 *   - Compose recomposes efficiently because comparisons are cheap (data class equals)
 *   - Easy to log, persist, or test ("did the screen show what I expected?")
 *
 * Each section has its OWN Resource because they load in parallel and can fail
 * independently — we don't want a flaky "upcoming" endpoint to break the whole page.
 */
data class HomeUiState(
    val trending: Resource<List<Movie>> = Resource.Loading,
    val popular: Resource<List<Movie>> = Resource.Loading,
    val topRated: Resource<List<Movie>> = Resource.Loading,
    val nowPlaying: Resource<List<Movie>> = Resource.Loading,
    val upcoming: Resource<List<Movie>> = Resource.Loading,
    val isRefreshing: Boolean = false
)

/**
 * ViewModel orchestrating the Home screen.
 *
 * Constructor injection of use cases keeps this class:
 *   - Easy to test (pass fake use cases)
 *   - Open for extension (add a new section by injecting a new use case)
 *   - Decoupled from data layer (talks only to domain)
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTrending: GetTrendingMoviesUseCase,
    private val getPopular: GetPopularMoviesUseCase,
    private val getTopRated: GetTopRatedMoviesUseCase,
    private val getNowPlaying: GetNowPlayingMoviesUseCase,
    private val getUpcoming: GetUpcomingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadAllSections()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadAllSections(isRefresh = true)
    }

    /**
     * Load all five sections in parallel using coroutines.
     * coroutineScope + async = parallel; awaitAll() ensures we wait for all to complete.
     *
     * Each section's result independently updates state — partial success is shown.
     */
    private fun loadAllSections(isRefresh: Boolean = false) {
        viewModelScope.launch {
            coroutineScope {
                async { loadTrending() }
                async { loadPopular() }
                async { loadTopRated() }
                async { loadNowPlaying() }
                async { loadUpcoming() }
            }
            if (isRefresh) {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }

    private suspend fun loadTrending() {
        _uiState.update { it.copy(trending = Resource.Loading) }
        runCatching { getTrending() }
            .onSuccess { _uiState.update { s -> s.copy(trending = Resource.Success(it)) } }
            .onFailure { _uiState.update { s -> s.copy(trending = Resource.Error(it.userMessage(), it)) } }
    }

    private suspend fun loadPopular() {
        _uiState.update { it.copy(popular = Resource.Loading) }
        runCatching { getPopular() }
            .onSuccess { _uiState.update { s -> s.copy(popular = Resource.Success(it)) } }
            .onFailure { _uiState.update { s -> s.copy(popular = Resource.Error(it.userMessage(), it)) } }
    }

    private suspend fun loadTopRated() {
        _uiState.update { it.copy(topRated = Resource.Loading) }
        runCatching { getTopRated() }
            .onSuccess { _uiState.update { s -> s.copy(topRated = Resource.Success(it)) } }
            .onFailure { _uiState.update { s -> s.copy(topRated = Resource.Error(it.userMessage(), it)) } }
    }

    private suspend fun loadNowPlaying() {
        _uiState.update { it.copy(nowPlaying = Resource.Loading) }
        runCatching { getNowPlaying() }
            .onSuccess { _uiState.update { s -> s.copy(nowPlaying = Resource.Success(it)) } }
            .onFailure { _uiState.update { s -> s.copy(nowPlaying = Resource.Error(it.userMessage(), it)) } }
    }

    private suspend fun loadUpcoming() {
        _uiState.update { it.copy(upcoming = Resource.Loading) }
        runCatching { getUpcoming() }
            .onSuccess { _uiState.update { s -> s.copy(upcoming = Resource.Success(it)) } }
            .onFailure { _uiState.update { s -> s.copy(upcoming = Resource.Error(it.userMessage(), it)) } }
    }

    /** Maps any throwable to a friendly message for the UI. */
    private fun Throwable.userMessage(): String = when (this) {
        is AppException -> message ?: "Something went wrong"
        else -> "Unexpected error: ${message ?: "unknown"}"
    }
}
