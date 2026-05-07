package com.example.moviemate.domain.usecase.search

import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Search movies by title.
 *
 * This use case showcases how use cases can hold simple business rules:
 *   - Trimming whitespace
 *   - Returning empty list for too-short queries (avoiding wasteful API calls)
 *
 * The ViewModel doesn't need to know these rules — it just calls invoke().
 * If we later decide "min query length is now 3", we change ONE place.
 */
class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    companion object {
        private const val MIN_QUERY_LENGTH = 2
    }

    suspend operator fun invoke(query: String, page: Int = 1): List<Movie> {
        val cleaned = query.trim()
        if (cleaned.length < MIN_QUERY_LENGTH) return emptyList()
        return repository.searchMovies(cleaned, page)
    }
}
