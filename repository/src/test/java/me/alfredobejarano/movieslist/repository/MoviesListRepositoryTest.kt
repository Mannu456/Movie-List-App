package me.alfredobejarano.movieslist.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.CachesLifeManager
import me.alfredobejarano.local.dao.MovieDao
import me.alfredobejarano.local.dao.MovieListIndexDao
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.core.MovieListType
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.model.MovieResult
import me.alfredobejarano.movieslist.remote.model.MoviesListResult
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesListRepositoryTest {
    @Mock
    private lateinit var mockMovieDaoDataSource: MovieDao
    @Mock
    private lateinit var mockMapper: Mapper<MovieResult, Movie>
    @Mock
    private lateinit var mockCachesLifeManager: CachesLifeManager
    @Mock
    private lateinit var mockApiDataSource: TheMoviesDBApiService
    @Mock
    private lateinit var mockMovieListIndexMovieDao: MovieListIndexDao
    private lateinit var testSubject: MoviesListRepository

    @Before
    fun setup() {
        testSubject = MoviesListRepository(
            mockMovieDaoDataSource,
            mockMapper,
            mockCachesLifeManager,
            mockApiDataSource,
            mockMovieListIndexMovieDao
        )
    }

    @Test
    fun getMoviesListBy_FromRemote_Success() {
        GlobalScope.launch {
            val listType = MovieListType.MOVIE_LIST_POPULAR

            val mockMovieResult = MovieResult(id = 123)
            val mockMovieListResult = MoviesListResult(results = listOf(mockMovieResult))
            val mockMovie = Movie(id = 123)

            Mockito.`when`(mockCachesLifeManager.listCacheIsValid(listType)).thenReturn(false)
            Mockito.`when`(mockApiDataSource.getPopularMovies()).thenReturn(mockMovieListResult)
            Mockito.`when`(mockMapper.map(mockMovieResult)).thenReturn(mockMovie)

            testSubject.getMoviesListBy(listType)

            Mockito.verify(
                mockMovieDaoDataSource.createOrUpdate(Mockito.any(Movie::class.java)),
                Mockito.times(mockMovieListResult.results?.size ?: 0)
            )

            Mockito.verify(
                mockMovieListIndexMovieDao.createOrUpdate(Mockito.any(MovieListIndex::class.java)),
                Mockito.times(1)
            )
            Mockito.verify(mockCachesLifeManager.generateListCache(listType), Mockito.times(1))
        }
    }

    @Test
    fun getMoviesListBy_FromRemote_Error() {
        GlobalScope.launch {
            val listType = MovieListType.MOVIE_LIST_POPULAR

            Mockito.`when`(mockCachesLifeManager.listCacheIsValid(listType)).thenReturn(false)
            Mockito.`when`(mockApiDataSource.getPopularMovies()).thenThrow(Exception())

            testSubject.getMoviesListBy(listType)

            Mockito.verify(mockMovieDaoDataSource.createOrUpdate(Mockito.any(Movie::class.java)), Mockito.never())

            Mockito.verify(
                mockMovieListIndexMovieDao.createOrUpdate(Mockito.any(MovieListIndex::class.java)),
                Mockito.never()
            )
            Mockito.verify(mockCachesLifeManager.generateListCache(listType), Mockito.never())
        }
    }

    @Test
    fun findMovieByTitle_Remote_Success() {
        GlobalScope.launch {
            val query = "awesome"

            val mockMovie = Movie(title = "My awesome movie")
            val mockMovieResult = MovieResult(title = "My awesome movie")

            val mockMovieListResult = MoviesListResult(results = listOf(mockMovieResult))

            Mockito.`when`(mockApiDataSource.searchMovie(query)).thenReturn(mockMovieListResult)
            Mockito.`when`(mockMapper.map(mockMovieResult)).thenReturn(mockMovie)

            Mockito.verify(
                mockMovieDaoDataSource.createOrUpdate(
                    Mockito.any(Movie::class.java)
                ), Mockito.times(mockMovieListResult.results?.size ?: 0)
            )

            val result = testSubject.findMovieByTitle(query)
            assert(result.isNotEmpty())
        }
    }

    @Test
    fun findMovieByTitle_Remote_Failure_Local_Success() {
        GlobalScope.launch {
            val query = "awesome"

            val mockMovie = Movie(title = "My awesome movie")
            val mockMovieResult = MovieResult(title = "My awesome movie")

            Mockito.`when`(mockApiDataSource.searchMovie(query)).thenThrow(Exception())
            Mockito.`when`(mockMovieDaoDataSource.findByTitle(query)).thenReturn(listOf(mockMovie))

            val result = testSubject.findMovieByTitle(query)

            Mockito.verify(mockMapper.map(mockMovieResult), Mockito.never())
            Mockito.verify(mockMovieDaoDataSource.createOrUpdate(mockMovie), Mockito.never())

            assert(result.isNotEmpty())
        }
    }

    @Test
    fun findMovieByTitle_Remote_Failure_Local_Failure() {
        GlobalScope.launch {
            val query = "awesome"

            val mockMovie = Movie(title = "My awesome movie")
            val mockMovieResult = MovieResult(title = "My awesome movie")

            Mockito.`when`(mockApiDataSource.searchMovie(query)).thenThrow(Exception())
            Mockito.`when`(mockMovieDaoDataSource.findByTitle(query)).thenThrow(Exception())

            val result = testSubject.findMovieByTitle(query)

            Mockito.verify(mockMapper.map(mockMovieResult), Mockito.never())
            Mockito.verify(mockMovieDaoDataSource.createOrUpdate(mockMovie), Mockito.never())

            assert(result.isEmpty())
        }
    }

    @Test
    fun getMovieListResult_Success() {
        GlobalScope.launch {
            val mockMovieId = 123
            val mockMovies = listOf(Movie())
            Mockito.`when`(mockMovieDaoDataSource.read(mockMovieId)).thenReturn(mockMovies)

            val result = testSubject.getMovieListResult(mockMovieId)
            assert(result == mockMovies.first())
        }
    }

    @Test
    fun getMovieListResult_Failure() {
        GlobalScope.launch {
            val mockMovieId = 123
            Mockito.`when`(mockMovieDaoDataSource.read(mockMovieId)).thenThrow(Exception())

            testSubject.getMovieListResult(mockMovieId)
            Mockito.verify(mockMovieDaoDataSource.read(mockMovieId), Mockito.times(1))
        }
    }
}