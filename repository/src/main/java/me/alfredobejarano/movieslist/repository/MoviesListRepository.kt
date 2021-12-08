package me.alfredobejarano.movieslist.repository

import me.alfredobejarano.local.CachesLifeManager
import me.alfredobejarano.local.dao.MovieDao
import me.alfredobejarano.local.dao.MovieListIndexDao
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.model.MovieResult
import javax.inject.Inject

/**
 * Created by alfredo on 2019-08-02.
 */
class MoviesListRepository @Inject constructor(
    private val movieDaoDataSource: MovieDao,
    private val mapper: Mapper<MovieResult, Movie>,
    private val cachesLifeManager: CachesLifeManager,
    private val apiDataSource: TheMoviesDBApiService,
    private val movieListIndexMovieDao: MovieListIndexDao
) {
    /**
     * Retrieves the list of movies by a given type.
     *
     * If the cache for the given list is still valid, it retrieves the
     * cached values from the local database, if the cache has expired, it
     * proceeds to retrieve it from the remote data source.
     *
     * @see MovieListType
     */
    suspend infix fun getMoviesListBy(type: MovieListType) =
        if (cachesLifeManager.listCacheIsValid(type)) {
            getMoviesListFromLocal(type)
        } else {
            generateMovieListCache(type, getMovieListFromRemote(type))
        }

    private suspend fun generateMovieListCache(type: MovieListType, movies: List<Movie>) = movies.apply {
        val movieIds = mutableListOf<Int>()
        forEach { movie ->
            movieIds.add(movie.id)
            movieDaoDataSource.createOrUpdate(movie)
        }

        movieListIndexMovieDao.createOrUpdate(MovieListIndex(type.ordinal, movieIds))
        cachesLifeManager.generateListCache(type)
    }

    /**
     * Retrieves the cached Movies.
     *
     * First, it proceeds to retrieve the index for the given move list and then
     * proceeds to read the cached movie data from the index movies Ids.
     */
    private suspend infix fun getMoviesListFromLocal(listType: MovieListType): List<Movie> {
        val movieListIndex = movieListIndexMovieDao.getListIndex(listType.ordinal)
        return movieListIndex.first().movies.map { movieId ->
            movieDaoDataSource.read(movieId).first()
        }
    }

    /**
     * Calls the appropiate endpoint for retrieving a list of movies.
     */
    private suspend infix fun getMovieListFromRemote(listType: MovieListType) = when (listType) {
        MovieListType.MOVIE_LIST_POPULAR -> apiDataSource.getPopularMovies().results
        MovieListType.MOVIE_LIST_UPCOMING -> apiDataSource.getUpcomingMovies().results
        MovieListType.MOVIE_LIST_TOP_RATED -> apiDataSource.getTopRatedMovies().results
    }?.map(mapper::map) ?: emptyList()

    suspend fun findMovieByTitle(query: String) = try {
        // Cache the movie results, for offline searches in the future.
        apiDataSource.searchMovie(query).results?.map(mapper::map)?.apply {
            forEach { movie -> movieDaoDataSource.createOrUpdate(movie) }
        } ?: emptyList()
    } catch (e: Exception) {
        // If an error happens with remote, return the local cached values.
        movieDaoDataSource.findByTitle("%$query%")
    }.sortedWith(compareBy { it.title }) // Sort the movies alphabetically

    /**
     * Retrieves the details of a Movie from the cache source.
     */
    suspend fun getMovieListResult(movieId: Int) = movieDaoDataSource.read(movieId).first()
}
