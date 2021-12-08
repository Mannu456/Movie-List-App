package me.alfredobejarano.movieslist.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.launchInIOForLiveData
import me.alfredobejarano.movieslist.domain.SearchMovieByTitleUseCase
import javax.inject.Inject

class MovieSearchViewModel @Inject constructor(
    private val searchMovieByTitleUseCase: SearchMovieByTitleUseCase
) : ViewModel() {
    fun searchMovieByTitle(query: String) = launchInIOForLiveData {
        searchMovieByTitleUseCase.searchMovieByTitle(query)
    }
}