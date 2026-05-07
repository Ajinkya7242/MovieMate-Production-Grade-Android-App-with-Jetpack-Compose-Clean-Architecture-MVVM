package com.example.moviemate.domain.usecase.movies

import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * USE CASES (also called Interactors) encapsulate ONE business operation each.
 *
 * Why use cases exist:
 *   - SOLID's Single Responsibility — one class, one reason to change
 *   - Reusable: multiple ViewModels can use GetPopularMoviesUseCase
 *   - Testable in isolation — fake the repository, assert use case behavior
 *   - Self-documenting: scanning the usecase folder = seeing what the app DOES
 *
 * The `operator fun invoke` trick lets callers do:
 *     val movies = getPopularMovies(page = 1)
 * instead of:
 *     val movies = getPopularMovies.execute(page = 1)
 *
 * It's just syntactic sugar but makes ViewModel code read more naturally.
 */
class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Movie> =
        repository.getPopularMovies(page)
}

class GetTopRatedMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Movie> =
        repository.getTopRatedMovies(page)
}

class GetNowPlayingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Movie> =
        repository.getNowPlayingMovies(page)
}

class GetUpcomingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int = 1): List<Movie> =
        repository.getUpcomingMovies(page)
}

class GetTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    /**
     * @param timeWindow "day" or "week"
     */
    suspend operator fun invoke(
        timeWindow: String = "week",
        page: Int = 1
    ): List<Movie> = repository.getTrendingMovies(timeWindow, page)
}
