package com.example.moviemate.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp Interceptor that automatically appends the TMDB api_key as a query
 * parameter on every outgoing request.
 *
 * Why this matters:
 *   - DRY: We don't repeat @Query("api_key") on every Retrofit method
 *   - Security: Easier to swap to a different auth scheme (e.g., Bearer token)
 *   - Single Responsibility: the API interface stays focused on endpoints,
 *     not authentication concerns
 *
 * @param apiKey TMDB v3 api_key, injected via constructor for testability
 */
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Append api_key query parameter to the request URL
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
