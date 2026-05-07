package com.example.moviemate.domain.model

/**
 * Domain models for credits and people.
 * All clean Kotlin — UI and ViewModel layers consume these.
 */

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    val profileUrl: String?,
    val order: Int
)

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    val department: String,
    val profileUrl: String?
)

data class MovieCredits(
    val movieId: Int,
    val cast: List<CastMember>,
    val crew: List<CrewMember>
) {
    /**
     * Convenience accessor for the director(s). A movie can have multiple
     * directors (e.g., the Wachowskis), so this returns a list.
     */
    val directors: List<CrewMember>
        get() = crew.filter { it.job.equals("Director", ignoreCase = true) }
}

data class PersonDetail(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String,
    val deathday: String,
    val placeOfBirth: String,
    val profileUrl: String?,
    val knownForDepartment: String,
    val homepage: String
) {
    val isAlive: Boolean get() = deathday.isBlank()
}

/**
 * Represents a movie a person worked on. Slimmer than full [Movie]
 * because the API returns less info in person credit responses.
 */
data class PersonMovieCredit(
    val movieId: Int,
    val title: String,
    val role: String,           // character name (cast) or job (crew)
    val posterUrl: String?,
    val releaseDate: String,
    val rating: Double,
    val isCast: Boolean         // true if acted, false if part of crew
) {
    val releaseYear: String
        get() = releaseDate.take(4).takeIf { it.length == 4 } ?: ""
}

/** Trailers and other videos for a movie. */
data class MovieVideo(
    val id: String,
    val key: String,        // YouTube video ID for embedding
    val name: String,
    val site: String,
    val type: String,
    val isOfficial: Boolean
) {
    val isYouTube: Boolean get() = site.equals("YouTube", ignoreCase = true)
    val youTubeUrl: String get() = "https://www.youtube.com/watch?v=$key"
}
