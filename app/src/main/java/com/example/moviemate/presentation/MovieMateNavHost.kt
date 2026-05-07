package com.example.moviemate.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.moviemate.core.navigation.Screen
import com.example.moviemate.presentation.castcrew.CastCrewScreen
import com.example.moviemate.presentation.details.MovieDetailScreen
import com.example.moviemate.presentation.favorites.FavoritesScreen
import com.example.moviemate.presentation.genremovies.GenreMoviesScreen
import com.example.moviemate.presentation.genres.GenresScreen
import com.example.moviemate.presentation.home.HomeScreen
import com.example.moviemate.presentation.onboarding.OnboardingScreen
import com.example.moviemate.presentation.person.PersonDetailScreen
import com.example.moviemate.presentation.search.SearchScreen
import com.example.moviemate.presentation.settings.SettingsScreen
import com.example.moviemate.presentation.splash.SplashScreen
import com.example.moviemate.presentation.watchlist.WatchlistScreen

/**
 * The single navigation graph for the entire app.
 *
 * Architecture choices:
 *   - All routes are defined here, in one file → easy to see app shape
 *   - Each composable() block sets up arguments and wires callbacks
 *   - Navigation actions are passed DOWN to screens; screens never own NavController
 *     (testability + reusability)
 *
 * popUpTo(...) { inclusive = true }: removes the splash from back stack so
 * back press from Home doesn't return to splash.
 */
@Composable
fun MovieMateNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ---------- Splash ----------
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Onboarding ----------
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------- Bottom-bar tabs ----------
        composable(Screen.Home.route) {
            HomeScreen(
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) },
                onGenresClick = { navController.navigate(Screen.Genres.route) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) }
            )
        }

        composable(Screen.Watchlist.route) {
            WatchlistScreen(
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        // ---------- Genres ----------
        composable(Screen.Genres.route) {
            GenresScreen(
                onBackClick = { navController.popBackStack() },
                onGenreClick = { genreId, name ->
                    navController.navigate(Screen.GenreMovies.createRoute(genreId, name))
                }
            )
        }

        // ---------- Genre Movies (with arguments) ----------
        composable(
            route = Screen.GenreMovies.route,
            arguments = listOf(
                navArgument(Screen.GenreMovies.ARG_GENRE_ID) { type = NavType.IntType },
                navArgument(Screen.GenreMovies.ARG_GENRE_NAME) { type = NavType.StringType }
            )
        ) {
            GenreMoviesScreen(
                onBackClick = { navController.popBackStack() },
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) }
            )
        }

        // ---------- Movie Detail ----------
        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(navArgument(Screen.MovieDetail.ARG_MOVIE_ID) { type = NavType.IntType })
        ) {
            MovieDetailScreen(
                onBackClick = { navController.popBackStack() },
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) },
                onCastClick = { personId -> navController.navigate(Screen.PersonDetail.createRoute(personId)) },
                onSeeAllCastClick = { movieId -> navController.navigate(Screen.CastCrew.createRoute(movieId)) }
            )
        }

        // ---------- Cast & Crew ----------
        composable(
            route = Screen.CastCrew.route,
            arguments = listOf(navArgument(Screen.CastCrew.ARG_MOVIE_ID) { type = NavType.IntType })
        ) {
            CastCrewScreen(
                onBackClick = { navController.popBackStack() },
                onPersonClick = { personId -> navController.navigate(Screen.PersonDetail.createRoute(personId)) }
            )
        }

        // ---------- Person Detail ----------
        composable(
            route = Screen.PersonDetail.route,
            arguments = listOf(navArgument(Screen.PersonDetail.ARG_PERSON_ID) { type = NavType.IntType })
        ) {
            PersonDetailScreen(
                onBackClick = { navController.popBackStack() },
                onMovieClick = { id -> navController.navigate(Screen.MovieDetail.createRoute(id)) }
            )
        }
    }
}
