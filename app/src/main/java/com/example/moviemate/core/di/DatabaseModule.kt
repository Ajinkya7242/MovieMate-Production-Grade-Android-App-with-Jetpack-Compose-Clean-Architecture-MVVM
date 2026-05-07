package com.example.moviemate.core.di

import android.content.Context
import androidx.room.Room
import com.example.moviemate.core.utils.Constants
import com.example.moviemate.data.local.dao.SavedMovieDao
import com.example.moviemate.data.local.database.MovieMateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the Room database and DAOs.
 *
 * @ApplicationContext lets Hilt inject the Application context safely
 * (never inject Activity context into a Singleton — memory leak!).
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieMateDatabase =
        Room.databaseBuilder(
            context,
            MovieMateDatabase::class.java,
            Constants.DATABASE_NAME
        )
            // For learning: wipe DB on schema change. Replace with proper migrations
            // before shipping to real users.
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSavedMovieDao(db: MovieMateDatabase): SavedMovieDao = db.savedMovieDao()
}
