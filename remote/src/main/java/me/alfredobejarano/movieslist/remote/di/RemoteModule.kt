package me.alfredobejarano.movieslist.remote.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiAuthInterceptor
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.map.MovieDetailsMapper
import me.alfredobejarano.movieslist.remote.map.MovieResultMapper
import me.alfredobejarano.movieslist.remote.model.MovieResult
import me.alfredobejarano.movieslist.remote.model.MovieSummary
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by alfredo on 2019-08-02.
 */
@Module
class RemoteModule(
    private val isDebug: Boolean,
    private val apiKey: String,
    private val baseURL: String,
    private val baseImageURL: String
) {
    private val authInterceptor by lazy { TheMoviesDBApiAuthInterceptor(apiKey) }

    private val gson: Gson by lazy { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }

    private val gsonConverter: GsonConverterFactory by lazy { GsonConverterFactory.create(gson) }

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            if (isDebug) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .baseUrl(baseURL)
            .build()
    }

    @Provides
    @Singleton
    fun provideTheMoviesDBApiService(): TheMoviesDBApiService =
        retrofit.create(TheMoviesDBApiService::class.java)

    @Provides
    fun provideMovieResultMapper(): Mapper<MovieResult, Movie> = MovieResultMapper(baseImageURL)

    @Provides
    fun provideMovieDetailsMapper(): Mapper<MovieSummary, MovieDetails> = MovieDetailsMapper(baseImageURL)
}