package com.example.moviemate.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from /movie/{id}/credits.
 * Contains both cast (actors) and crew (directors, writers, etc.).
 */
@Serializable
data class CreditsResponseDto(
    @SerialName("id") val id: Int,
    @SerialName("cast") val cast: List<CastDto> = emptyList(),
    @SerialName("crew") val crew: List<CrewDto> = emptyList()
)

@Serializable
data class CastDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("character") val character: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("order") val order: Int = 0,
    @SerialName("known_for_department") val knownForDepartment: String? = null
)

@Serializable
data class CrewDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("job") val job: String? = null,
    @SerialName("department") val department: String? = null,
    @SerialName("profile_path") val profilePath: String? = null
)

/**
 * Response from /person/{id}.
 */
@Serializable
data class PersonDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("biography") val biography: String? = null,
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("deathday") val deathday: String? = null,
    @SerialName("place_of_birth") val placeOfBirth: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("homepage") val homepage: String? = null
)

/**
 * Response from /person/{id}/movie_credits — what films a person worked on.
 */
@Serializable
data class PersonMovieCreditsDto(
    @SerialName("id") val id: Int,
    @SerialName("cast") val cast: List<PersonMovieCastDto> = emptyList(),
    @SerialName("crew") val crew: List<PersonMovieCrewDto> = emptyList()
)

@Serializable
data class PersonMovieCastDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("character") val character: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0
)

@Serializable
data class PersonMovieCrewDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("job") val job: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0
)

/**
 * Response from /movie/{id}/videos — trailers etc.
 */
@Serializable
data class VideosResponseDto(
    @SerialName("id") val id: Int,
    @SerialName("results") val results: List<VideoDto>
)

@Serializable
data class VideoDto(
    @SerialName("id") val id: String,
    @SerialName("key") val key: String,         // YouTube video key
    @SerialName("name") val name: String,
    @SerialName("site") val site: String,       // "YouTube" or "Vimeo"
    @SerialName("type") val type: String,       // "Trailer", "Teaser", etc.
    @SerialName("official") val official: Boolean = false
)
