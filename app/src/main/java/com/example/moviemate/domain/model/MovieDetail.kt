package com.example.moviemate.domain.model

/**
 * Rich domain model for a movie detail screen.
 * Builds on top of [Movie] but adds extra info available only when fetching one movie.
 */
data class MovieDetail(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val tagline: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val runtimeMinutes: Int,
    val status: String,
    val rating: Double,
    val voteCount: Int,
    val budget: Long,
    val revenue: Long,
    val homepage: String,
    val genres: List<Genre>,
    val productionCompanies: List<String>,
    val originalLanguage: String
) {
    /** Returns runtime as "2h 15m" or "—" if unknown. */
    val displayRuntime: String
        get() {
            if (runtimeMinutes <= 0) return "—"
            val hours = runtimeMinutes / 60
            val minutes = runtimeMinutes % 60
            return when {
                hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
                hours > 0 -> "${hours}h"
                else -> "${minutes}m"
            }
        }

    val releaseYear: String
        get() = releaseDate.take(4).takeIf { it.length == 4 } ?: ""

    val displayRating: String
        get() = String.format("%.1f", rating)
}

data class Genre(
    val id: Int,
    val name: String
)
