package com.example.moviemate.core.navigation

/**
 * Type-safe navigation destinations for the entire app.
 *
 * Why a sealed class for routes:
 *   - Single source of truth for every route string in the app
 *   - Compile-time safety — typos in route strings would break the build
 *   - Centralized argument naming, encoding, and decoding logic
 *   - Easy to refactor: rename a route, IDE updates every reference
 *
 * Pattern:
 *   - `route` is the navigation pattern with placeholders (e.g., "movie/{movieId}")
 *   - `createRoute(...)` builds an actual URL with substituted values
 *   - The destination's argument names are kept as constants for the NavGraph to use
 */
sealed class Screen(val route: String) {

    // ----- Top-level / startup -----
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")

    // ----- Bottom-bar root tabs -----
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Watchlist : Screen("watchlist")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")

    // ----- Other screens -----
    data object Genres : Screen("genres")

    data object MovieDetail : Screen("movie/{movieId}") {
        const val ARG_MOVIE_ID = "movieId"
        fun createRoute(movieId: Int) = "movie/$movieId"
    }

    data object CastCrew : Screen("movie/{movieId}/credits") {
        const val ARG_MOVIE_ID = "movieId"
        fun createRoute(movieId: Int) = "movie/$movieId/credits"
    }

    data object PersonDetail : Screen("person/{personId}") {
        const val ARG_PERSON_ID = "personId"
        fun createRoute(personId: Int) = "person/$personId"
    }

    data object GenreMovies : Screen("genre/{genreId}/{genreName}") {
        const val ARG_GENRE_ID = "genreId"
        const val ARG_GENRE_NAME = "genreName"
        fun createRoute(genreId: Int, genreName: String) =
            "genre/$genreId/${java.net.URLEncoder.encode(genreName, "UTF-8")}"
    }
}

/**
 * Bottom navigation items. The order here = the order on the screen.
 * Icons are referenced as Material Icons constants so they stay vector and free.
 */
enum class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconName: String  // resolved to actual icon in BottomBar composable
) {
    HOME(Screen.Home, "Home", "home"),
    SEARCH(Screen.Search, "Search", "search"),
    WATCHLIST(Screen.Watchlist, "Watchlist", "bookmark"),
    FAVORITES(Screen.Favorites, "Favorites", "favorite"),
    SETTINGS(Screen.Settings, "Settings", "settings")
}
