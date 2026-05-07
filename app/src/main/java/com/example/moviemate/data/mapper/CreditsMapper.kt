package com.example.moviemate.data.mapper

import com.example.moviemate.core.utils.Constants
import com.example.moviemate.data.remote.dto.CastDto
import com.example.moviemate.data.remote.dto.CreditsResponseDto
import com.example.moviemate.data.remote.dto.CrewDto
import com.example.moviemate.data.remote.dto.PersonDetailDto
import com.example.moviemate.data.remote.dto.PersonMovieCastDto
import com.example.moviemate.data.remote.dto.PersonMovieCreditsDto
import com.example.moviemate.data.remote.dto.PersonMovieCrewDto
import com.example.moviemate.data.remote.dto.VideoDto
import com.example.moviemate.domain.model.CastMember
import com.example.moviemate.domain.model.CrewMember
import com.example.moviemate.domain.model.MovieCredits
import com.example.moviemate.domain.model.MovieVideo
import com.example.moviemate.domain.model.PersonDetail
import com.example.moviemate.domain.model.PersonMovieCredit

fun CastDto.toDomain(): CastMember = CastMember(
    id = id,
    name = name,
    character = character.orEmpty(),
    profileUrl = Constants.profileUrl(profilePath),
    order = order
)

fun CrewDto.toDomain(): CrewMember = CrewMember(
    id = id,
    name = name,
    job = job.orEmpty(),
    department = department.orEmpty(),
    profileUrl = Constants.profileUrl(profilePath)
)

fun CreditsResponseDto.toDomain(): MovieCredits = MovieCredits(
    movieId = id,
    cast = cast.map { it.toDomain() }.sortedBy { it.order },
    crew = crew.map { it.toDomain() }
)

fun PersonDetailDto.toDomain(): PersonDetail = PersonDetail(
    id = id,
    name = name,
    biography = biography.orEmpty(),
    birthday = birthday.orEmpty(),
    deathday = deathday.orEmpty(),
    placeOfBirth = placeOfBirth.orEmpty(),
    profileUrl = Constants.profileUrl(profilePath),
    knownForDepartment = knownForDepartment.orEmpty(),
    homepage = homepage.orEmpty()
)

fun VideoDto.toDomain(): MovieVideo = MovieVideo(
    id = id,
    key = key,
    name = name,
    site = site,
    type = type,
    isOfficial = official
)

fun PersonMovieCastDto.toDomain(): PersonMovieCredit = PersonMovieCredit(
    movieId = id,
    title = title,
    role = character.orEmpty(),
    posterUrl = Constants.posterUrl(posterPath),
    releaseDate = releaseDate.orEmpty(),
    rating = voteAverage,
    isCast = true
)

fun PersonMovieCrewDto.toDomain(): PersonMovieCredit = PersonMovieCredit(
    movieId = id,
    title = title,
    role = job.orEmpty(),
    posterUrl = Constants.posterUrl(posterPath),
    releaseDate = releaseDate.orEmpty(),
    rating = voteAverage,
    isCast = false
)

/**
 * Combines cast + crew credits, removing duplicates (same movie may appear in both).
 * Sorted by release date descending (newest first).
 */
fun PersonMovieCreditsDto.toDomain(): List<PersonMovieCredit> {
    val castCredits = cast.map { it.toDomain() }
    val crewCredits = crew.map { it.toDomain() }
    return (castCredits + crewCredits)
        .distinctBy { it.movieId }
        .sortedByDescending { it.releaseDate }
}
