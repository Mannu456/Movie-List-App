package me.alfredobejarano.movieslist.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.repository.MoviesListRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieListUseCaseTest {
    @Mock
    private lateinit var mockMoviesListRepository: MoviesListRepository
    private lateinit var testsubject: GetMovieListUseCase

    @Before
    fun setup() {
        testsubject = GetMovieListUseCase(mockMoviesListRepository)
    }

    @Test
    fun getMovieList_Success() {
        val mockType = MovieListType.MOVIE_LIST_UPCOMING
        val mockMovieList = listOf(Movie(id = 123))

        GlobalScope.launch {
            Mockito.`when`(mockMoviesListRepository getMoviesListBy mockType)
                .thenReturn(mockMovieList)

            val result = testsubject.getMovieList(mockType)
            assert(result.payload?.isNotEmpty() == true)
        }
    }

    @Test
    fun getMovieList_Failure() {
        val mockType = MovieListType.MOVIE_LIST_TOP_RATED

        GlobalScope.launch {
            Mockito.`when`(mockMoviesListRepository getMoviesListBy mockType)
                .thenThrow(Exception())

            val result = testsubject.getMovieList(mockType)
            assert(result.error?.isNotEmpty() == true)
        }
    }
}