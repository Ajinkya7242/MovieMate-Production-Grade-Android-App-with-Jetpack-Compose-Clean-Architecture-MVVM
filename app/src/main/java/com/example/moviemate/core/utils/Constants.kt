package com.example.moviemate.core.utils

import com.example.moviemate.BuildConfig

/**
 * App-wide constants.
 *
 * Centralizing these values:
 *   - Single source of truth (DRY principle)
 *   - Easy to change (e.g., switch image quality)
 *   - Prevents magic strings/numbers scattered throughout code
 */
object Constants {

    // ---------- Image URLs ----------
    // TMDB serves images at different sizes. We use these size suffixes:
    // w92, w154, w185, w342, w500, w780, original (for posters)
    // w300, w780, w1280, original (for backdrops)
    private val IMAGE_BASE = BuildConfig.TMDB_IMAGE_BASE_URL

    /** Build full poster URL for grid/list views (good balance of quality vs size). */
    fun posterUrl(path: String?): String? = path?.let { "${IMAGE_BASE}w500$it" }

    /** Smaller poster for thumbnails. */
    fun posterThumbnailUrl(path: String?): String? = path?.let { "${IMAGE_BASE}w185$it" }

    /** Backdrop image used in detail screen header. */
    fun backdropUrl(path: String?): String? = path?.let { "${IMAGE_BASE}w780$it" }

    /** Profile picture for actors/directors. */
    fun profileUrl(path: String?): String? = path?.let { "${IMAGE_BASE}w185$it" }

    // ---------- Pagination ----------
    const val PAGE_SIZE = 20  // TMDB returns 20 items per page by default
    const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2

    // ---------- Database ----------
    const val DATABASE_NAME = "moviemate_database"

    // ---------- DataStore ----------
    const val USER_PREFERENCES = "user_preferences"
}
