package me.alfredobejarano.movieslist.domain

import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.repository.MovieDetailsRepository
import me.alfredobejarano.movieslist.repository.MoviesListRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val moviesListRepository: MoviesListRepository
) {
    /**
     * Retrieves the details of a movie, if something wrong happens while fetching the movie details,
     * this use case will attempt to retrieve the details of that movie using the MovieList cache, (this get cached
     * if the user saw the movie in a previous screen or made a search for it). If the movie isn't found
     * in the MovieList cache either, an error will be responded.
     *
     * @param movieId ID of the movie to retrieve its details.
     */
    suspend fun getMovieDetails(movieId: Int) = interact {
        try {
            movieDetailsRepository.getMovieDetails(movieId)
        } catch (e: Exception) {
            moviesListRepository.getMovieListResult(movieId).run {
                MovieDetails(
                    id = id,
                    title = title,
                    posterURL = posterURL
                )
            }
        }
    }
}