package com.example.moviemate.core.utils

/**
 * Domain-specific exceptions.
 *
 * Why custom exceptions instead of throwing IOException everywhere?
 *   - The DOMAIN layer must NOT depend on Android/Java specifics
 *   - These exceptions are framework-agnostic (Clean Architecture rule)
 *   - The data layer maps Retrofit/IO exceptions → these domain exceptions
 *   - The presentation layer maps these → user-friendly UI messages
 *
 * This is the Dependency Rule from Clean Architecture: dependencies point inward.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    /** Generic network error - server unreachable, timeout, etc. */
    class NetworkException(
        message: String = "Network unavailable. Please check your connection.",
        cause: Throwable? = null
    ) : AppException(message, cause)

    /** Server returned a non-2xx HTTP status code. */
    class ServerException(
        val code: Int,
        message: String = "Server error occurred ($code)",
        cause: Throwable? = null
    ) : AppException(message, cause)

    /** Data parsing failed - response shape doesn't match what we expected. */
    class ParseException(
        message: String = "Failed to parse server response",
        cause: Throwable? = null
    ) : AppException(message, cause)

    /** Requested resource not found (e.g., movie ID doesn't exist). */
    class NotFoundException(
        message: String = "Requested resource was not found"
    ) : AppException(message)

    /** Catch-all for anything we didn't anticipate. */
    class UnknownException(
        message: String = "Something went wrong",
        cause: Throwable? = null
    ) : AppException(message, cause)
}
