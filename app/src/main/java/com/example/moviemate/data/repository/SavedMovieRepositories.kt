package com.example.moviemate.data.repository

import com.example.moviemate.data.local.dao.SavedMovieDao
import com.example.moviemate.data.local.entity.SavedMovieEntity
import com.example.moviemate.data.local.entity.toDomain
import com.example.moviemate.data.local.entity.toSavedEntity
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.repository.FavoritesRepository
import com.example.moviemate.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Watchlist repository — stores movies the user wants to watch later.
 *
 * Note that we share one DAO between both lists (DRY) but use different
 * `type` discriminators. This trade-off:
 *   - Pro: less code, one schema
 *   - Con: a typo in `type` could mix up lists
 *   - Mitigation: `type` is a constant on [SavedMovieEntity], not a free-form string
 */
@Singleton
class WatchlistRepositoryImpl @Inject constructor(
    private val dao: SavedMovieDao
) : WatchlistRepository {

    private val type = SavedMovieEntity.TYPE_WATCHLIST

    override fun observeWatchlist(): Flow<List<Movie>> =
        dao.observeByType(type).map { entities -> entities.map { it.toDomain() } }

    override fun isInWatchlist(movieId: Int): Flow<Boolean> =
        dao.isSaved(movieId, type)

    override suspend fun addToWatchlist(movie: Movie) {
        dao.insert(movie.toSavedEntity(type))
    }

    override suspend fun removeFromWatchlist(movieId: Int) {
        dao.delete(movieId, type)
    }

    override suspend fun clearWatchlist() {
        dao.clearByType(type)
    }
}

/**
 * Favorites repository — movies the user loved.
 * Mirrors [WatchlistRepositoryImpl] but uses the favorite type.
 */
@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dao: SavedMovieDao
) : FavoritesRepository {

    private val type = SavedMovieEntity.TYPE_FAVORITE

    override fun observeFavorites(): Flow<List<Movie>> =
        dao.observeByType(type).map { entities -> entities.map { it.toDomain() } }

    override fun isFavorite(movieId: Int): Flow<Boolean> =
        dao.isSaved(movieId, type)

    override suspend fun addToFavorites(movie: Movie) {
        dao.insert(movie.toSavedEntity(type))
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        dao.delete(movieId, type)
    }

    override suspend fun clearFavorites() {
        dao.clearByType(type)
    }
}
