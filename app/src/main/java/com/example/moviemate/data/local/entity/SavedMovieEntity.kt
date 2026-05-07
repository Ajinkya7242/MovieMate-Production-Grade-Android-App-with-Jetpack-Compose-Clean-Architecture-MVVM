package com.example.moviemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviemate.domain.model.Movie

/**
 * Room Entity for saved movies (used by watchlist and favorites).
 *
 * Why a separate entity from the domain Movie:
 *   - Room annotations are infrastructure concerns; domain shouldn't know about them
 *   - We can add DB-specific columns (savedAt timestamp) without polluting the domain
 *   - Database schema can evolve independently of API/domain shape
 *
 * The `tableName` and `type` columns work together to let one table store
 * both watchlist and favorite movies. We could split into two tables, but
 * a single table with a discriminator is simpler and more flexible.
 */
@Entity(
    tableName = "saved_movies",
    primaryKeys = ["id", "type"]   // composite PK: same movie can be in both lists
)
data class SavedMovieEntity(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val rating: Double,
    val voteCount: Int,
    val type: String,           // "watchlist" or "favorite"
    val savedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val TYPE_WATCHLIST = "watchlist"
        const val TYPE_FAVORITE = "favorite"
    }
}

/** Convert entity → domain Movie (UI never sees the entity). */
fun SavedMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    releaseDate = releaseDate,
    rating = rating,
    voteCount = voteCount,
    genreIds = emptyList()
)

/** Convert domain Movie → entity (for inserting into DB). */
fun Movie.toSavedEntity(type: String): SavedMovieEntity = SavedMovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    releaseDate = releaseDate,
    rating = rating,
    voteCount = voteCount,
    type = type
)
