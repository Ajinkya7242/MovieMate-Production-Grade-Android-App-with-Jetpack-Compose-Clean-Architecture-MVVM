package com.example.moviemate.data.mapper

import com.example.moviemate.core.utils.Constants
import com.example.moviemate.data.remote.dto.GenreDto
import com.example.moviemate.data.remote.dto.MovieDetailDto
import com.example.moviemate.data.remote.dto.MovieDto
import com.example.moviemate.domain.model.Genre
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.model.MovieDetail

/**
 * Mappers convert between layer-specific representations:
 *   - DTOs (data layer) → Domain models (domain layer)
 *
 * Why mappers are kept in extension-function form:
 *   - Discoverable: dto.toDomain() reads naturally
 *   - Stateless: pure functions, trivially testable
 *   - Single Responsibility: each function does ONE conversion
 */

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview.orEmpty(),
    posterUrl = Constants.posterUrl(posterPath),
    backdropUrl = Constants.backdropUrl(backdropPath),
    releaseDate = releaseDate.orEmpty(),
    rating = voteAverage,
    voteCount = voteCount,
    genreIds = genreIds
)

fun List<MovieDto>.toDomain(): List<Movie> = map { it.toDomain() }

fun GenreDto.toDomain(): Genre = Genre(id = id, name = name)

fun List<GenreDto>.toDomainGenres(): List<Genre> = map { it.toDomain() }

fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title,
    originalTitle = originalTitle.orEmpty(),
    tagline = tagline.orEmpty(),
    overview = overview.orEmpty(),
    posterUrl = Constants.posterUrl(posterPath),
    backdropUrl = Constants.backdropUrl(backdropPath),
    releaseDate = releaseDate.orEmpty(),
    runtimeMinutes = runtime ?: 0,
    status = status.orEmpty(),
    rating = voteAverage,
    voteCount = voteCount,
    budget = budget,
    revenue = revenue,
    homepage = homepage.orEmpty(),
    genres = genres.toDomainGenres(),
    productionCompanies = productionCompanies.map { it.name },
    originalLanguage = originalLanguage.orEmpty()
)
