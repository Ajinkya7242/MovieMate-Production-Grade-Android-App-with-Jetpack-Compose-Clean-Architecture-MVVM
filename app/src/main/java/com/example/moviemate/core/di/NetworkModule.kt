package com.example.moviemate.core.di

import com.example.moviemate.BuildConfig
import com.example.moviemate.data.remote.api.TmdbApi
import com.example.moviemate.data.remote.interceptor.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module that provides networking dependencies.
 *
 * @InstallIn(SingletonComponent::class) → these dependencies live as long as the app.
 * They're created once and reused everywhere.
 *
 * Why split modules by concern (Network, Database, Repository, etc.):
 *   - Easier to find what provides what
 *   - Each module reflects ONE bounded responsibility
 *   - Can be selectively replaced in tests (e.g., a TestNetworkModule)
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        // Be lenient: TMDB occasionally returns extra fields we don't care about.
        // ignoreUnknownKeys lets the app keep working when TMDB adds new fields.
        ignoreUnknownKeys = true
        coerceInputValues = true        // null → default value (e.g., null Int → 0)
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            // Log full bodies in DEBUG; silence in production for security/perf.
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(BuildConfig.TMDB_API_KEY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)        // appends api_key first
        .addInterceptor(loggingInterceptor)       // logs the FINAL request URL
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.TMDB_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideTmdbApi(retrofit: Retrofit): TmdbApi = retrofit.create(TmdbApi::class.java)
}
