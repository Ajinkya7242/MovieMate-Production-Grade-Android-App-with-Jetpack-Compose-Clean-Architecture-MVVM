package com.example.moviemate.core.di

import com.example.moviemate.data.repository.FavoritesRepositoryImpl
import com.example.moviemate.data.repository.MovieRepositoryImpl
import com.example.moviemate.data.repository.WatchlistRepositoryImpl
import com.example.moviemate.domain.repository.FavoritesRepository
import com.example.moviemate.domain.repository.MovieRepository
import com.example.moviemate.domain.repository.WatchlistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds repository INTERFACES to their concrete IMPLEMENTATIONS.
 *
 * This is the linchpin of Dependency Inversion in our app:
 *   - ViewModels and use cases inject `MovieRepository` (the interface)
 *   - Hilt resolves it to `MovieRepositoryImpl` because of this binding
 *   - In tests, we can swap the binding to a fake implementation
 *
 * Why @Binds (abstract function) instead of @Provides (regular function):
 *   - More efficient: Hilt skips an extra layer of indirection
 *   - Use @Binds whenever you just need to say "interface X → class Y"
 *   - Use @Provides when construction is non-trivial (Retrofit setup, etc.)
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    @Binds
    @Singleton
    abstract fun bindWatchlistRepository(impl: WatchlistRepositoryImpl): WatchlistRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
