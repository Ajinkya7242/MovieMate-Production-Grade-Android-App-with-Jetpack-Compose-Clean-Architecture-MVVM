package com.example.moviemate.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moviemate.core.navigation.BottomNavItem

/**
 * Bottom navigation bar shown only on the 5 root tabs.
 *
 * Implementation notes:
 *   - Uses currentBackStackEntryAsState to track which tab is selected
 *     (more accurate than tracking it manually in state)
 *   - launchSingleTop avoids stacking the same destination
 *   - popUpTo(startDestination) keeps the back stack clean — back press
 *     from any tab returns to Home, not to a chain of tabs
 *   - saveState/restoreState preserves scroll position and other UI state
 *     when switching tabs
 */
@Composable
fun MovieMateBottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        BottomNavItem.entries.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = iconFor(item),
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

private fun iconFor(item: BottomNavItem): ImageVector = when (item) {
    BottomNavItem.HOME -> Icons.Filled.Home
    BottomNavItem.SEARCH -> Icons.Filled.Search
    BottomNavItem.WATCHLIST -> Icons.Filled.Bookmark
    BottomNavItem.FAVORITES -> Icons.Filled.Favorite
    BottomNavItem.SETTINGS -> Icons.Filled.Settings
}
