package me.alfredobejarano.movieslist.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.launchInIOForLiveData
import me.alfredobejarano.movieslist.domain.GetMovieDetailsUseCase
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(private val movieDetailsUseCase: GetMovieDetailsUseCase) : ViewModel() {
    /**
     * Retrieves the details of a movie using its Id.
     */
    fun getMovieDetails(movieId: Int) = launchInIOForLiveData { movieDetailsUseCase.getMovieDetails(movieId) }
}