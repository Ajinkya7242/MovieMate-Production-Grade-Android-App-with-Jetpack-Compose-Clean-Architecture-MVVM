package com.example.moviemate.domain.model

/**
 * Domain model for a Movie.
 *
 * Clean Architecture rules followed here:
 *   1. Pure Kotlin — no Android, Retrofit, Room, or kotlinx.serialization imports
 *   2. Optimized for our app's needs (e.g., precomputed full URLs, formatted year)
 *   3. Immutable — uses `val` everywhere; safer in concurrent contexts
 *
 * The data layer maps DTOs/Entities → this model.
 * The UI layer reads only this model.
 *
 * If TMDB renames a field tomorrow, only the data-layer mapper changes.
 * The rest of the codebase is insulated from that churn.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val rating: Double,
    val voteCount: Int,
    val genreIds: List<Int> = emptyList()
) {
    /** Year extracted from the release date, e.g. "2024". Empty if unparseable. */
    val releaseYear: String
        get() = releaseDate.take(4).takeIf { it.length == 4 } ?: ""

    /** Display-ready rating string, e.g. "8.4". */
    val displayRating: String
        get() = String.format("%.1f", rating)
}
