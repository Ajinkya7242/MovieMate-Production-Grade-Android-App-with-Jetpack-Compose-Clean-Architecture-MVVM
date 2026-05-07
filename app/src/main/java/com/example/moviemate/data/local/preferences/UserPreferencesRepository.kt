package com.example.moviemate.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.moviemate.core.theme.ThemePreference
import com.example.moviemate.core.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Property delegate creates a SINGLE DataStore instance per Context.
 * Defining it at the file level (top-level) ensures we don't accidentally
 * create multiple DataStores, which would corrupt the file.
 */
private val Context.dataStore by preferencesDataStore(name = Constants.USER_PREFERENCES)

/**
 * Stores user-level preferences such as theme choice and onboarding completion.
 *
 * Why DataStore instead of SharedPreferences:
 *   - Type-safe, async API
 *   - Coroutines + Flow first-class
 *   - No more `apply()` vs `commit()` confusion
 *   - Officially recommended by Google for Compose apps
 */
@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val THEME = stringPreferencesKey("theme_preference")
        val ONBOARDING_COMPLETED = stringPreferencesKey("onboarding_completed")
    }

    /** Reactive theme preference — emits whenever the user changes the setting. */
    val themePreference: Flow<ThemePreference> = context.dataStore.data.map { prefs ->
        when (prefs[Keys.THEME]) {
            ThemePreference.LIGHT.name -> ThemePreference.LIGHT
            ThemePreference.DARK.name -> ThemePreference.DARK
            else -> ThemePreference.SYSTEM
        }
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] == "true"
    }

    suspend fun setThemePreference(theme: ThemePreference) {
        context.dataStore.edit { prefs -> prefs[Keys.THEME] = theme.name }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed.toString()
        }
    }
}
