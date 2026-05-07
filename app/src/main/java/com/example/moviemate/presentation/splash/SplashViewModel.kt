package com.example.moviemate.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.data.local.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Splash screen ViewModel.
 *
 * Responsibility (Single Responsibility):
 *   - Decide whether to show onboarding or jump to Home, based on preferences
 *
 * Why a ViewModel for a splash:
 *   - Survives configuration changes (rotation during the splash delay)
 *   - Lets us test the routing logic independently of UI
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo: StateFlow<String?> = _navigateTo.asStateFlow()

    init {
        decideStartDestination()
    }

    private fun decideStartDestination() {
        viewModelScope.launch {
            // Brief artistic pause so the splash actually feels like a splash
            delay(SPLASH_DELAY_MS)

            val onboardingDone = preferencesRepository.onboardingCompleted.first()
            _navigateTo.value = if (onboardingDone) {
                Screen.Home.route
            } else {
                Screen.Onboarding.route
            }
        }
    }

    private companion object {
        const val SPLASH_DELAY_MS = 1200L
    }
}
