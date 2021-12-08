package me.alfredobejarano.movieslist.domain

import me.alfredobejarano.movieslist.repository.MoviesListRepository
import javax.inject.Inject

class SearchMovieByTitleUseCase @Inject constructor(private val repository: MoviesListRepository) {
    /**
     * Searches in both local and remote data sources for movies that better match the query in its title.
     */
    suspend fun searchMovieByTitle(query: String) = interact {
        repository.findMovieByTitle(query)
    }
}