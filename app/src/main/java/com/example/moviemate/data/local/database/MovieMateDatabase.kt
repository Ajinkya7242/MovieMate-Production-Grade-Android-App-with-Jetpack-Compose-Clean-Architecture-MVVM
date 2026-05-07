package com.example.moviemate.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviemate.data.local.dao.SavedMovieDao
import com.example.moviemate.data.local.entity.SavedMovieEntity

/**
 * The Room database. This is the main access point for the SQLite store.
 *
 * Versioning notes:
 *   - `version = 1` for the initial release
 *   - When you add/change columns, bump version and provide a Migration
 *   - For learning, `fallbackToDestructiveMigration()` (in DI module) wipes data
 *     on schema changes — fine in dev, NEVER use in production
 *
 * Why a single database for both watchlist + favorites:
 *   - Same domain (saved movies)
 *   - Reduces complexity (one DB to manage, one place for migrations)
 *   - The `type` column discriminates between the two lists
 */
@Database(
    entities = [SavedMovieEntity::class],
    version = 1,
    exportSchema = false   // For learning. In real apps, set true and commit schemas to git.
)
abstract class MovieMateDatabase : RoomDatabase() {
    abstract fun savedMovieDao(): SavedMovieDao
}
