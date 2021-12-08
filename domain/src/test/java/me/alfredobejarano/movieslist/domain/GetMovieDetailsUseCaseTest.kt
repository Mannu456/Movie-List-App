package me.alfredobejarano.movieslist.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.repository.MovieDetailsRepository
import me.alfredobejarano.movieslist.repository.MoviesListRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieDetailsUseCaseTest {
    private lateinit var testSubject: GetMovieDetailsUseCase
    @Mock
    private lateinit var mockMoviesListRepository: MoviesListRepository
    @Mock
    private lateinit var mockMovieDetailsRepository: MovieDetailsRepository

    @Before
    fun setup() {
        testSubject = GetMovieDetailsUseCase(mockMovieDetailsRepository, mockMoviesListRepository)
    }

    @Test
    fun getMovieDetails_Success() {
        val mockMovieId = 123
        val mockMovieDetails = MovieDetails(mockMovieId)
        GlobalScope.launch {
            Mockito.`when`(mockMovieDetailsRepository.getMovieDetails(mockMovieId))
                .thenReturn(mockMovieDetails)

            val resource = testSubject.getMovieDetails(mockMovieId)

            assert(resource.payload == mockMovieDetails)
        }
    }

    @Test
    fun getMovieDetails_Failure_Cache_Success() {
        val mockMovieId = 123
        val mockMovie = Movie(123)
        val mockMovieDetails = MovieDetails(123)

        GlobalScope.launch {
            Mockito.`when`(mockMovieDetailsRepository.getMovieDetails(mockMovieId))
                .thenThrow(Exception())
            Mockito.`when`(mockMoviesListRepository.getMovieListResult(mockMovieId))
                .thenReturn(mockMovie)

            val resource = testSubject.getMovieDetails(mockMovieId)

            assert(resource.payload == mockMovieDetails)
        }
    }

    @Test
    fun getMovieDetails_Failure_Cache_Error() {
        val mockMovieId = 123
        val mockErrorMessage = "Mock error message"

        GlobalScope.launch {
            Mockito.`when`(mockMovieDetailsRepository.getMovieDetails(mockMovieId))
                .thenThrow(Exception())
            Mockito.`when`(mockMoviesListRepository.getMovieListResult(mockMovieId))
                .thenThrow(Exception(mockErrorMessage))

            val resource = testSubject.getMovieDetails(mockMovieId)

            assert(resource.error == mockErrorMessage)
        }
    }
}