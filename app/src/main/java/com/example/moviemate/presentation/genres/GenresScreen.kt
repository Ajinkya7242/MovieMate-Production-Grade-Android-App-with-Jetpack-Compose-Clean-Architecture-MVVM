package com.example.moviemate.presentation.genres

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviemate.core.components.ErrorState
import com.example.moviemate.core.components.LoadingIndicator
import com.example.moviemate.core.utils.Resource

/**
 * Genre browse screen — a colorful grid of genre tiles.
 * Each tile is given a deterministic color based on its index, so the same
 * genre always gets the same color (visual stability).
 */
private val genreGradients = listOf(
    listOf(Color(0xFFFF6B6B), Color(0xFFEE5A6F)),    // red-pink
    listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)),    // teal
    listOf(Color(0xFFA8E6CF), Color(0xFF56CD8A)),    // green
    listOf(Color(0xFFFFD93D), Color(0xFFE9A800)),    // gold
    listOf(Color(0xFF6C5CE7), Color(0xFF4834D4)),    // purple
    listOf(Color(0xFFFD79A8), Color(0xFFE84393)),    // pink
    listOf(Color(0xFF74B9FF), Color(0xFF0984E3)),    // blue
    listOf(Color(0xFFFAB1A0), Color(0xFFE17055))     // peach
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresScreen(
    onBackClick: () -> Unit,
    onGenreClick: (Int, String) -> Unit,
    viewModel: GenresViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse by Genre") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorState(
                message = s.message,
                onRetry = { viewModel.retry() }
            )
            is Resource.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(padding).fillMaxSize()
                ) {
                    items(s.data, key = { it.id }) { genre ->
                        // Pick gradient based on stable id-derived index
                        val gradient = genreGradients[genre.id % genreGradients.size]

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.5f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(brush = Brush.linearGradient(gradient))
                                .clickable { onGenreClick(genre.id, genre.name) }
                                .padding(16.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(
                                text = genre.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
