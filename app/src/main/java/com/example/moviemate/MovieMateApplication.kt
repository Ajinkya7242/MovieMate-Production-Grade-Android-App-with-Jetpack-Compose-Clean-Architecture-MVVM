package com.example.moviemate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class that bootstraps Hilt for the entire app.
 *
 * The @HiltAndroidApp annotation triggers Hilt's code generation, including
 * a base class for the application that serves as the application-level
 * dependency container.
 *
 * This is the entry point for Dagger Hilt's dependency graph.
 */
@HiltAndroidApp
class MovieMateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Place global initialization here (Timber, crash reporting, etc.)
    }
}
