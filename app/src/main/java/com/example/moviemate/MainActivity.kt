package com.example.moviemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.moviemate.core.theme.MovieMateTheme
import com.example.moviemate.core.theme.ThemePreference
import com.example.moviemate.data.local.preferences.UserPreferencesRepository
import com.example.moviemate.presentation.MovieMateApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * The single Activity in this app.
 *
 * We follow the "single activity" architecture pattern recommended by Google:
 * - One Activity hosts the entire Compose UI
 * - All navigation happens via Navigation Compose
 * - This makes shared element transitions, state hoisting, and DI much simpler
 *
 * @AndroidEntryPoint tells Hilt to inject dependencies into this Activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate (Android 12+ API)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Read user theme preference reactively
            val themePref by userPreferencesRepository.themePreference
                .collectAsState(initial = ThemePreference.SYSTEM)

            val darkTheme = when (themePref) {
                ThemePreference.LIGHT -> false
                ThemePreference.DARK -> true
                ThemePreference.SYSTEM -> isSystemInDarkTheme()
            }

            MovieMateTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieMateApp()
                }
            }
        }
    }
}
