package com.example.moviemate.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.utils.AppException
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.usecase.search.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** UI state for the Search screen. */
data class SearchUiState(
    val query: String = "",
    val results: Resource<List<Movie>> = Resource.Success(emptyList()),
    val recentSearches: List<String> = emptyList()
)

/**
 * Search ViewModel with proper debouncing.
 *
 * Why debounce + flatMapLatest:
 *   - debounce(300ms): wait for typing to pause before hitting the API
 *   - distinctUntilChanged: don't re-search if text didn't actually change
 *   - flatMapLatest: cancel the previous search if a new query arrives
 *
 * This combination prevents:
 *   - One API call per keystroke (battery + bandwidth disaster)
 *   - Race conditions where stale results overwrite fresh ones
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovies: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /** Internal flow for the query string. Drives the search pipeline below. */
    private val queryFlow = MutableStateFlow("")

    init {
        // Reactive search pipeline
        queryFlow
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .flatMapLatest { q ->
                flow {
                    if (q.isBlank()) {
                        emit(Resource.Success(emptyList()))
                    } else {
                        emit(Resource.Loading)
                        try {
                            val results = searchMovies(q)
                            emit(Resource.Success(results))
                        } catch (e: AppException) {
                            emit(Resource.Error(e.message ?: "Search failed", e))
                        } catch (e: Exception) {
                            emit(Resource.Error("Unexpected error", e))
                        }
                    }
                }
            }
            .onEach { result ->
                _uiState.update { it.copy(results = result) }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
        queryFlow.value = newQuery
    }

    fun clearQuery() = onQueryChange("")

    private companion object {
        const val DEBOUNCE_MS = 350L
    }
}
