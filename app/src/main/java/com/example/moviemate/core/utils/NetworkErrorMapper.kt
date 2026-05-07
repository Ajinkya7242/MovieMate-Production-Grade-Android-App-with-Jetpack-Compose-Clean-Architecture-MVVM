package com.example.moviemate.core.utils

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Maps low-level networking/parsing exceptions to domain-level [AppException]s.
 *
 * Why this exists (Dependency Inversion principle):
 *   - The domain layer must not know about Retrofit, OkHttp, or kotlinx.serialization
 *   - This mapper is part of the data layer (it knows about networking)
 *   - It produces domain-friendly errors (it knows about AppException)
 *
 * Usage in repositories:
 *   try { return apiCall() }
 *   catch (e: Exception) { throw e.toAppException() }
 */
fun Throwable.toAppException(): AppException = when (this) {
    is AppException -> this  // already mapped, pass through

    is UnknownHostException ->
        AppException.NetworkException("No internet connection", this)

    is SocketTimeoutException ->
        AppException.NetworkException("Connection timed out. Please try again.", this)

    is HttpException -> {
        when (code()) {
            404 -> AppException.NotFoundException()
            in 500..599 -> AppException.ServerException(
                code = code(),
                message = "Server is having trouble. Please try again later.",
                cause = this
            )
            else -> AppException.ServerException(
                code = code(),
                message = "Request failed (${code()})",
                cause = this
            )
        }
    }

    is SerializationException ->
        AppException.ParseException(cause = this)

    is IOException ->
        AppException.NetworkException(cause = this)

    else ->
        AppException.UnknownException(
            message = message ?: "Something went wrong",
            cause = this
        )
}
