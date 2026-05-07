package com.example.moviemate.data.repository

import com.example.moviemate.core.utils.toAppException
import com.example.moviemate.data.mapper.toDomain
import com.example.moviemate.data.mapper.toDomainGenres
import com.example.moviemate.data.remote.api.TmdbApi
import com.example.moviemate.domain.model.Genre
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.model.MovieDetail
import com.example.moviemate.domain.model.MovieVideo
import com.example.moviemate.domain.model.PersonDetail
import com.example.moviemate.domain.model.PersonMovieCredit
import com.example.moviemate.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [MovieRepository].
 *
 * Responsibilities (Single Responsibility):
 *   - Delegate network calls to the [TmdbApi]
 *   - Map DTOs to domain models
 *   - Translate framework exceptions to domain [AppException]
 *
 * What it does NOT do (kept narrow on purpose):
 *   - No caching logic here (could be added with a memory cache or Room)
 *   - No business rules (those live in use cases)
 *   - No UI concerns (no Strings, no formatters that need locale, etc.)
 *
 * Why @Singleton:
 *   - Repositories are stateless service layers, one per app is enough
 *   - Avoids creating new Retrofit clients/OkHttp clients per call
 */
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : MovieRepository {

    /**
     * `safeApiCall` centralizes try/catch around every network call.
     * Without this helper, every method would have a duplicated try/catch (DRY violation).
     */
    private inline fun <T> safeApiCall(block: () -> T): T = try {
        block()
    } catch (e: Exception) {
        throw e.toAppException()
    }

    // ---- Lists ----
    override suspend fun getPopularMovies(page: Int): List<Movie> = safeApiCall {
        api.getPopularMovies(page).results.toDomain()
    }

    override suspend fun getTopRatedMovies(page: Int): List<Movie> = safeApiCall {
        api.getTopRatedMovies(page).results.toDomain()
    }

    override suspend fun getNowPlayingMovies(page: Int): List<Movie> = safeApiCall {
        api.getNowPlayingMovies(page).results.toDomain()
    }

    override suspend fun getUpcomingMovies(page: Int): List<Movie> = safeApiCall {
        api.getUpcomingMovies(page).results.toDomain()
    }

    override suspend fun getTrendingMovies(timeWindow: String, page: Int): List<Movie> = safeApiCall {
        api.getTrendingMovies(timeWindow, page).results.toDomain()
    }

    // ---- Search ----
    override suspend fun searchMovies(query: String, page: Int): List<Movie> = safeApiCall {
        if (query.isBlank()) return@safeApiCall emptyList()
        api.searchMovies(query.trim(), page).results.toDomain()
    }

    // ---- Details ----
    override suspend fun getMovieDetails(movieId: Int): MovieDetail = safeApiCall {
        api.getMovieDetails(movieId).toDomain()
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCredits = safeApiCall {
        api.getMovieCredits(movieId).toDomain()
    }

    override suspend fun getSimilarMovies(movieId: Int, page: Int): List<Movie> = safeApiCall {
        api.getSimilarMovies(movieId, page).results.toDomain()
    }

    override suspend fun getRecommendedMovies(movieId: Int, page: Int): List<Movie> = safeApiCall {
        api.getRecommendedMovies(movieId, page).results.toDomain()
    }

    override suspend fun getMovieVideos(movieId: Int): List<MovieVideo> = safeApiCall {
        api.getMovieVideos(movieId).results.map { it.toDomain() }
    }

    // ---- Genres ----
    override suspend fun getMovieGenres(): List<Genre> = safeApiCall {
        api.getMovieGenres().genres.toDomainGenres()
    }

    override suspend fun getMoviesByGenre(genreId: Int, page: Int): List<Movie> = safeApiCall {
        api.discoverByGenre(genreId, page).results.toDomain()
    }

    // ---- Person ----
    override suspend fun getPersonDetails(personId: Int): PersonDetail = safeApiCall {
        api.getPersonDetails(personId).toDomain()
    }

    override suspend fun getPersonMovieCredits(personId: Int): List<PersonMovieCredit> = safeApiCall {
        api.getPersonMovieCredits(personId).toDomain()
    }
}
