package com.example.moviemate.data.remote.api

import com.example.moviemate.data.remote.dto.CreditsResponseDto
import com.example.moviemate.data.remote.dto.GenreListResponseDto
import com.example.moviemate.data.remote.dto.MovieDetailDto
import com.example.moviemate.data.remote.dto.MovieListResponseDto
import com.example.moviemate.data.remote.dto.PersonDetailDto
import com.example.moviemate.data.remote.dto.PersonMovieCreditsDto
import com.example.moviemate.data.remote.dto.VideosResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface defining all TMDB endpoints we need.
 *
 * Why an interface (Interface Segregation principle from SOLID):
 *   - High-level code depends on this abstraction, not on Retrofit specifics
 *   - Easy to swap implementations (real HTTP, fake for tests, etc.)
 *   - Single Responsibility: this file is ONLY about defining what the API exposes
 *
 * The api_key Query parameter is added automatically by [ApiKeyInterceptor],
 * so we don't repeat it on every endpoint here.
 */
interface TmdbApi {

    // ============== Movie Lists ==============

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("trending/movie/{timeWindow}")
    suspend fun getTrendingMovies(
        @Path("timeWindow") timeWindow: String = "week",  // "day" or "week"
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    // ============== Search ==============

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    // ============== Movie Details ==============

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetailDto

    @GET("movie/{movieId}/credits")
    suspend fun getMovieCredits(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US"
    ): CreditsResponseDto

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("movie/{movieId}/recommendations")
    suspend fun getRecommendedMovies(
        @Path("movieId") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en-US"
    ): VideosResponseDto

    // ============== Genres ==============

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "en-US"
    ): GenreListResponseDto

    @GET("discover/movie")
    suspend fun discoverByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String = "en-US"
    ): MovieListResponseDto

    // ============== Person ==============

    @GET("person/{personId}")
    suspend fun getPersonDetails(
        @Path("personId") personId: Int,
        @Query("language") language: String = "en-US"
    ): PersonDetailDto

    @GET("person/{personId}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("personId") personId: Int,
        @Query("language") language: String = "en-US"
    ): PersonMovieCreditsDto
}
