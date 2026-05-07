package com.example.moviemate.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviemate.core.navigation.BottomNavItem
import com.example.moviemate.presentation.common.MovieMateBottomBar

/**
 * Top-level composable for the entire app.
 *
 * Responsibilities:
 *   - Hosts the NavController
 *   - Decides when to show the bottom bar (only on root tabs)
 *
 * Why the bottom bar visibility logic lives here, not in NavHost:
 *   - Scaffold needs to know about the bar at this level
 *   - NavHost itself should stay agnostic of UI chrome
 *
 * Each screen handles its own padding via its own Scaffold, so we don't
 * pass `padding` from this Scaffold down to NavHost. This keeps screens
 * self-contained and able to be previewed independently.
 */
@Composable
fun MovieMateApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Show bottom bar only on the 5 root tabs
    val showBottomBar = BottomNavItem.entries.any { it.screen.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MovieMateBottomBar(navController = navController)
            }
        }
    ) { padding ->
        MovieMateNavHost(
            navController = navController,
            modifier = Modifier.padding(bottom = 60.dp)
        )
    }
}
