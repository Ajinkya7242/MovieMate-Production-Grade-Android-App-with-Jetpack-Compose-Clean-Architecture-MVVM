package com.example.moviemate.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.CastMember
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.model.MovieDetail
import com.example.moviemate.domain.model.MovieVideo
import com.example.moviemate.domain.usecase.details.GetMovieCreditsUseCase
import com.example.moviemate.domain.usecase.details.GetMovieDetailsUseCase
import com.example.moviemate.domain.usecase.details.GetMovieVideosUseCase
import com.example.moviemate.domain.usecase.details.GetRecommendedMoviesUseCase
import com.example.moviemate.domain.usecase.details.GetSimilarMoviesUseCase
import com.example.moviemate.domain.usecase.favorites.IsFavoriteUseCase
import com.example.moviemate.domain.usecase.favorites.ToggleFavoriteUseCase
import com.example.moviemate.domain.usecase.watchlist.IsInWatchlistUseCase
import com.example.moviemate.domain.usecase.watchlist.ToggleWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the movie detail screen.
 *
 * Notice we have ONE Resource for the "header" data (detail + credits + videos
 * loaded together) but separate Resources for related lists. This is a UX
 * choice: the screen is unusable without the detail, but is still useful even
 * if "similar movies" fails to load.
 */
data class MovieDetailUiState(
    val detailState: Resource<MovieDetail> = Resource.Loading,
    val cast: List<CastMember> = emptyList(),
    val credits: MovieCredits? = null,
    val videos: List<MovieVideo> = emptyList(),
    val similar: Resource<List<Movie>> = Resource.Loading,
    val recommended: Resource<List<Movie>> = Resource.Loading,
    val isInWatchlist: Boolean = false,
    val isFavorite: Boolean = false
)

/**
 * Detail ViewModel uses [SavedStateHandle] to read the movieId nav argument.
 * This is the official Android pattern — no need for the Activity to pass args.
 *
 * It also exposes Flow<Boolean> from the watchlist/favorites use cases as
 * StateFlow, which means the heart/bookmark icon updates instantly when toggled.
 */
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val getMovieCredits: GetMovieCreditsUseCase,
    private val getMovieVideos: GetMovieVideosUseCase,
    private val getSimilarMovies: GetSimilarMoviesUseCase,
    private val getRecommendedMovies: GetRecommendedMoviesUseCase,
    private val isInWatchlist: IsInWatchlistUseCase,
    private val isFavorite: IsFavoriteUseCase,
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    /** Read movieId from the nav argument. !! is safe — route requires it. */
    private val movieId: Int = checkNotNull(savedStateHandle[Screen.MovieDetail.ARG_MOVIE_ID])

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    /** Reactive: emits true/false whenever the DB state changes. */
    val watchlistState: StateFlow<Boolean> = isInWatchlist(movieId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val favoriteState: StateFlow<Boolean> = isFavorite(movieId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    init {
        loadAll()
    }

    fun retry() = loadAll()

    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(detailState = Resource.Loading) }

            // Try the critical detail fetch first — without it, screen is empty
            try {
                val detail = getMovieDetails(movieId)
                _uiState.update { it.copy(detailState = Resource.Success(detail)) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(detailState = Resource.Error(e.message ?: "Failed to load movie", e))
                }
                return@launch  // can't continue without the main detail
            }

            // Now fetch ancillary info in parallel — failures are silent
            coroutineScope {
                async { loadCredits() }
                async { loadVideos() }
                async { loadSimilar() }
                async { loadRecommended() }
            }
        }
    }

    private suspend fun loadCredits() {
        runCatching { getMovieCredits(movieId) }
            .onSuccess { credits ->
                _uiState.update {
                    it.copy(
                        cast = credits.cast.take(20),
                        credits = credits
                    )
                }
            }
    }

    private suspend fun loadVideos() {
        runCatching { getMovieVideos(movieId) }
            .onSuccess { vids ->
                _uiState.update { it.copy(videos = vids.filter { v -> v.isYouTube }) }
            }
    }

    private suspend fun loadSimilar() {
        _uiState.update { it.copy(similar = Resource.Loading) }
        runCatching { getSimilarMovies(movieId) }
            .onSuccess { _uiState.update { s -> s.copy(similar = Resource.Success(it)) } }
            .onFailure {
                _uiState.update { s ->
                    s.copy(similar = Resource.Error(it.message ?: "Failed to load", it))
                }
            }
    }

    private suspend fun loadRecommended() {
        _uiState.update { it.copy(recommended = Resource.Loading) }
        runCatching { getRecommendedMovies(movieId) }
            .onSuccess { _uiState.update { s -> s.copy(recommended = Resource.Success(it)) } }
            .onFailure {
                _uiState.update { s ->
                    s.copy(recommended = Resource.Error(it.message ?: "Failed to load", it))
                }
            }
    }

    fun onToggleWatchlist() {
        val detailRes = _uiState.value.detailState
        if (detailRes !is Resource.Success) return

        viewModelScope.launch {
            val movie = detailRes.data.toMovie()
            toggleWatchlist(movie, watchlistState.value)
        }
    }

    fun onToggleFavorite() {
        val detailRes = _uiState.value.detailState
        if (detailRes !is Resource.Success) return

        viewModelScope.launch {
            val movie = detailRes.data.toMovie()
            toggleFavorite(movie, favoriteState.value)
        }
    }

    /** Map MovieDetail down to a regular Movie for saving. */
    private fun MovieDetail.toMovie() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = releaseDate,
        rating = rating,
        voteCount = voteCount,
        genreIds = genres.map { it.id }
    )
}
