package com.example.moviemate.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO (Data Transfer Object) representing the response shape from TMDB's
 * paginated movie list endpoints (e.g. /movie/popular, /movie/top_rated).
 *
 * Why DTOs are separate from domain models:
 *   - DTOs mirror the EXACT JSON structure from the API
 *   - Domain models are clean Kotlin types optimized for our app's needs
 *   - This decoupling means: if TMDB changes their API, only our mapper breaks
 *   - The domain/UI layers don't need to change at all
 *
 * @SerialName lets us map JSON field names like "poster_path" to Kotlin
 * camelCase like "posterPath", keeping our code idiomatic.
 */
@Serializable
data class MovieListResponseDto(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

/** A single movie from a list response. */
@Serializable
data class MovieDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("original_language") val originalLanguage: String? = null
)
