package com.example.moviemate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviemate.data.local.entity.SavedMovieEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the saved_movies table.
 *
 * Best practices applied:
 *   - Read methods return Flow → reactive UI updates
 *   - Write methods are `suspend` → safe to call from any coroutine
 *   - OnConflictStrategy.REPLACE → idempotent inserts (re-adding doesn't crash)
 *   - One DAO covers both watchlist + favorites (filtered by `type` column)
 */
@Dao
interface SavedMovieDao {

    @Query("SELECT * FROM saved_movies WHERE type = :type ORDER BY savedAt DESC")
    fun observeByType(type: String): Flow<List<SavedMovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_movies WHERE id = :movieId AND type = :type)")
    fun isSaved(movieId: Int, type: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: SavedMovieEntity)

    @Query("DELETE FROM saved_movies WHERE id = :movieId AND type = :type")
    suspend fun delete(movieId: Int, type: String)

    @Query("DELETE FROM saved_movies WHERE type = :type")
    suspend fun clearByType(type: String)

    @Query("SELECT COUNT(*) FROM saved_movies WHERE type = :type")
    fun countByType(type: String): Flow<Int>
}
