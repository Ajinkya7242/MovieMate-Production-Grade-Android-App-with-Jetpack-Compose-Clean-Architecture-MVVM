package com.example.moviemate.domain.usecase.details

import com.example.moviemate.domain.model.Movie
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.model.MovieDetail
import com.example.moviemate.domain.model.MovieVideo
import com.example.moviemate.domain.model.PersonDetail
import com.example.moviemate.domain.model.PersonMovieCredit
import com.example.moviemate.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetail =
        repository.getMovieDetails(movieId)
}

class GetMovieCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieCredits =
        repository.getMovieCredits(movieId)
}

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int = 1): List<Movie> =
        repository.getSimilarMovies(movieId, page)
}

class GetRecommendedMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int = 1): List<Movie> =
        repository.getRecommendedMovies(movieId, page)
}

class GetMovieVideosUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    /** Returns trailers (preferring official ones first), then teasers. */
    suspend operator fun invoke(movieId: Int): List<MovieVideo> {
        val all = repository.getMovieVideos(movieId)
        // Prioritize: official trailers > unofficial trailers > teasers > others
        return all.sortedWith(
            compareByDescending<MovieVideo> { it.type.equals("Trailer", ignoreCase = true) }
                .thenByDescending { it.isOfficial }
                .thenByDescending { it.type.equals("Teaser", ignoreCase = true) }
        )
    }
}

class GetPersonDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(personId: Int): PersonDetail =
        repository.getPersonDetails(personId)
}

class GetPersonMovieCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(personId: Int): List<PersonMovieCredit> =
        repository.getPersonMovieCredits(personId)
}
