package com.example.moviemate.domain.usecase.movies

import com.example.moviemate.domain.model.Genre
import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): List<Genre> = repository.getMovieGenres()
}

class GetMoviesByGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(genreId: Int, page: Int = 1): List<Movie> =
        repository.getMoviesByGenre(genreId, page)
}
