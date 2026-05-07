package com.example.moviemate.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviemate.core.components.ErrorState
import com.example.moviemate.core.components.MoviePosterCard
import com.example.moviemate.core.components.SectionTitle
import com.example.moviemate.core.components.ShimmerBox
import com.example.moviemate.core.utils.Resource
import com.example.moviemate.domain.model.Movie

/**
 * Home screen — a vertical scroll of horizontal sections, Netflix-style.
 *
 * State hoisting: the screen takes navigation callbacks from the parent.
 * It doesn't decide where to navigate; it just signals user intent.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    onGenresClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MovieMate",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = onGenresClick) {
                        Icon(Icons.Filled.Category, contentDescription = "Genres")
                    }
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 8.dp)
        ) {
            HorizontalSection(
                title = "Trending This Week",
                resource = state.trending,
                onMovieClick = onMovieClick
            )
            HorizontalSection(
                title = "Popular",
                resource = state.popular,
                onMovieClick = onMovieClick
            )
            HorizontalSection(
                title = "Now Playing",
                resource = state.nowPlaying,
                onMovieClick = onMovieClick
            )
            HorizontalSection(
                title = "Top Rated",
                resource = state.topRated,
                onMovieClick = onMovieClick
            )
            HorizontalSection(
                title = "Coming Soon",
                resource = state.upcoming,
                onMovieClick = onMovieClick
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

/**
 * One horizontal-scrolling section of movies.
 *
 * It's stateless: takes a Resource and renders the appropriate UI for each state.
 * That's the magic of Resource — the UI is just a `when` statement.
 */
@Composable
private fun HorizontalSection(
    title: String,
    resource: Resource<List<Movie>>,
    onMovieClick: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        SectionTitle(text = title)

        when (resource) {
            is Resource.Loading -> ShimmerRow()
            is Resource.Error -> {
                Text(
                    text = "Couldn't load: ${resource.message}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            is Resource.Success -> {
                if (resource.data.isEmpty()) {
                    Text(
                        text = "No movies available",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = resource.data,
                            key = { it.id }   // key prop optimizes recomposition
                        ) { movie ->
                            MoviePosterCard(
                                movie = movie,
                                onClick = { onMovieClick(movie.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Loading placeholder row showing shimmering poster shapes. */
@Composable
private fun ShimmerRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(5) {
            ShimmerBox(
                modifier = Modifier
                    .height(240.dp)
                    .width(160.dp)
            )
        }
    }
}
