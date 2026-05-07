package com.example.moviemate.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Full movie details returned from /movie/{id} endpoint.
 *
 * Has more fields than the basic [MovieDto] because TMDB returns extra
 * info when you query a single movie (runtime, genres expanded, budget, etc.).
 */
@Serializable
data class MovieDetailDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    @SerialName("popularity") val popularity: Double = 0.0,
    @SerialName("budget") val budget: Long = 0,
    @SerialName("revenue") val revenue: Long = 0,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("production_companies") val productionCompanies: List<ProductionCompanyDto> = emptyList(),
    @SerialName("production_countries") val productionCountries: List<ProductionCountryDto> = emptyList(),
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto> = emptyList(),
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("original_language") val originalLanguage: String? = null
)

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

@Serializable
data class GenreListResponseDto(
    @SerialName("genres") val genres: List<GenreDto>
)

@Serializable
data class ProductionCompanyDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("origin_country") val originCountry: String? = null
)

@Serializable
data class ProductionCountryDto(
    @SerialName("iso_3166_1") val iso31661: String,
    @SerialName("name") val name: String
)

@Serializable
data class SpokenLanguageDto(
    @SerialName("iso_639_1") val iso6391: String,
    @SerialName("name") val name: String,
    @SerialName("english_name") val englishName: String? = null
)
