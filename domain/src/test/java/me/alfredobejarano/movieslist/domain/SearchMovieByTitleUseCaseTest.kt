package me.alfredobejarano.movieslist.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.repository.MoviesListRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchMovieByTitleUseCaseTest {
    @Mock
    private lateinit var mockMoviesListRepository: MoviesListRepository
    private lateinit var testSubject: SearchMovieByTitleUseCase

    @Before
    fun setup() {
        testSubject = SearchMovieByTitleUseCase(mockMoviesListRepository)
    }

    @Test
    fun searchMovieByTitle_Success() {
        val query = "movie"
        val mockList = listOf(Movie(title = "My movie"))

        GlobalScope.launch {
            Mockito.`when`(mockMoviesListRepository.findMovieByTitle(query))
                .thenReturn(mockList)

            val result = testSubject.searchMovieByTitle(query)
            assert(result.payload?.isNotEmpty() == true)
        }
    }

    @Test
    fun searchMovieByTitle_Error() {
        val query = "movie"

        GlobalScope.launch {
            Mockito.`when`(mockMoviesListRepository.findMovieByTitle(query))
                .thenThrow(Exception("Error"))

            val result = testSubject.searchMovieByTitle(query)
            assert(result.error?.isNotEmpty() == true)
        }
    }
}