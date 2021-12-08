package me.alfredobejarano.movieslist.repository

import me.alfredobejarano.local.CachesLifeManager
import me.alfredobejarano.local.dao.MovieDetailsDao
import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.map.MovieDetailsMapper
import me.alfredobejarano.movieslist.remote.model.MovieSummary
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val localDaoDataSource: MovieDetailsDao,
    private val apiDataSource: TheMoviesDBApiService,
    private val cachesLifeManager: CachesLifeManager,
    private val mapper: Mapper<MovieSummary, MovieDetails>
) {
    companion object {
        private const val VIDEO_TYPE_TRAILER = "Trailer"
        private const val VIDEO_SITE_YOUTUBE = "YouTube"
    }

    /**
     * Retrieves the key of first available trailer for the movie hosted in YouTube.
     * @param movieId Id of the movie to search videos for.
     * @return String containing the YouTube key for the first available trailer or an empty string for no trailer.
     */
    private suspend fun getMovieVideo(movieId: Int): String {
        val movieVideos = apiDataSource.getMovieVideos(movieId).videoResults?.filter { movieVideoResult ->
            VIDEO_SITE_YOUTUBE == movieVideoResult.site && VIDEO_TYPE_TRAILER == movieVideoResult.type
        } ?: emptyList()

        return if (movieVideos.isNotEmpty()) {
            movieVideos.first().key ?: ""
        } else {
            ""
        }
    }

    /**
     * Retrieves the movie summary from the internet and proceeds to cache the response.
     */
    private suspend fun getMovieDetailsRemote(movieId: Int): MovieDetails {
        (mapper as? MovieDetailsMapper)?.movieVideoKey = getMovieVideo(movieId)
        val movie = mapper.map(apiDataSource.getMovieDetails(movieId))

        localDaoDataSource.createOrUpdate(movie)
        cachesLifeManager.generateMovieDetailsCache(movieId)

        return movie
    }

    /**
     * Retrieves the movie details from the local storage.
     */
    private suspend fun getMovieDetailsLocal(movieId: Int) = localDaoDataSource.read(movieId).first()

    /**
     * Retrieves the movie details from the best data source available.
     */
    suspend fun getMovieDetails(movieId: Int) = if (cachesLifeManager.movieCacheIsValid(movieId)) {
        getMovieDetailsLocal(movieId)
    } else {
        getMovieDetailsRemote(movieId)
    }
}