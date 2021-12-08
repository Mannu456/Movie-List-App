package me.alfredobejarano.movieslist.domain

import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.repository.MoviesListRepository
import javax.inject.Inject

/**
 * Retrieves a list of movies by a given type.
 *
 * Created by alfredo on 2019-08-02.
 */
class GetMovieListUseCase @Inject constructor(private val repository: MoviesListRepository) {
    suspend fun getMovieList(type: MovieListType) = interact {
        repository getMoviesListBy type
    }
}