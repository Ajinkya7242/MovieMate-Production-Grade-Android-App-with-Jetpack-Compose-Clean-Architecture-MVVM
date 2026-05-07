package com.example.moviemate.domain.repository

import com.example.moviemate.domain.model.Genre
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.model.MovieDetail
import com.example.moviemate.domain.model.MovieVideo
import com.example.moviemate.domain.model.PersonDetail
import com.example.moviemate.domain.model.PersonMovieCredit

/**
 * Repository CONTRACT for fetching movie data.
 *
 * This is the heart of the Dependency Inversion Principle (the "D" in SOLID):
 *   - HIGH-LEVEL modules (use cases) depend on this interface
 *   - LOW-LEVEL modules (the concrete repository implementation) implement it
 *   - Both depend on abstractions, neither depends on the other directly
 *
 * Why we have an interface here at all:
 *   - The domain layer can be unit tested with a fake implementation
 *   - We could swap the data source later (REST → GraphQL, REST → gRPC) and
 *     the domain layer wouldn't notice
 *
 * Note: methods can throw [com.example.moviemate.core.utils.AppException].
 * The repository implementation translates network/parsing errors into these.
 */
interface MovieRepository {

    // ---- Lists ----
    suspend fun getPopularMovies(page: Int): List<Movie>
    suspend fun getTopRatedMovies(page: Int): List<Movie>
    suspend fun getNowPlayingMovies(page: Int): List<Movie>
    suspend fun getUpcomingMovies(page: Int): List<Movie>
    suspend fun getTrendingMovies(timeWindow: String, page: Int): List<Movie>

    // ---- Search ----
    suspend fun searchMovies(query: String, page: Int): List<Movie>

    // ---- Details ----
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    suspend fun getMovieCredits(movieId: Int): MovieCredits
    suspend fun getSimilarMovies(movieId: Int, page: Int): List<Movie>
    suspend fun getRecommendedMovies(movieId: Int, page: Int): List<Movie>
    suspend fun getMovieVideos(movieId: Int): List<MovieVideo>

    // ---- Genres ----
    suspend fun getMovieGenres(): List<Genre>
    suspend fun getMoviesByGenre(genreId: Int, page: Int): List<Movie>

    // ---- Person ----
    suspend fun getPersonDetails(personId: Int): PersonDetail
    suspend fun getPersonMovieCredits(personId: Int): List<PersonMovieCredit>
}
