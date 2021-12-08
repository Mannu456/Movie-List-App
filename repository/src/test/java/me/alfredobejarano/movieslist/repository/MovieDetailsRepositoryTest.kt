package me.alfredobejarano.movieslist.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.CachesLifeManager
import me.alfredobejarano.local.dao.MovieDetailsDao
import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.remote.TheMoviesDBApiService
import me.alfredobejarano.movieslist.remote.map.Mapper
import me.alfredobejarano.movieslist.remote.model.MovieSummary
import me.alfredobejarano.movieslist.remote.model.MovieVideo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailsRepositoryTest {
    private val scope = GlobalScope

    @Mock
    private lateinit var mockMovieDetailsDao: MovieDetailsDao
    @Mock
    private lateinit var mockApiDataSource: TheMoviesDBApiService
    @Mock
    private lateinit var mockCachesLifeManager: CachesLifeManager
    @Mock
    private lateinit var mockMapper: Mapper<MovieSummary, MovieDetails>

    private lateinit var testSubject: MovieDetailsRepository

    @Before
    fun setUp() {
        testSubject = MovieDetailsRepository(mockMovieDetailsDao, mockApiDataSource, mockCachesLifeManager, mockMapper)
    }

    @Test
    fun getMovieDetails_getFromRemote_Success() {
        scope.launch {
            val mockMovieId = 123
            val mockMovievideo = MovieVideo()
            val mockMovieSummary = MovieSummary()
            val mockMovieDetails = MovieDetails()

            Mockito.`when`(mockCachesLifeManager.movieCacheIsValid(mockMovieId))
                .thenReturn(false)
            Mockito.`when`(mockApiDataSource.getMovieVideos(mockMovieId)).thenReturn(mockMovievideo)
            Mockito.`when`(mockApiDataSource.getMovieDetails(mockMovieId)).thenReturn(mockMovieSummary)
            Mockito.`when`(mockMapper.map(mockMovieSummary)).thenReturn(mockMovieDetails)

            testSubject.getMovieDetails(mockMovieId)

            Mockito.verify(mockMovieDetailsDao, Mockito.times(1)).createOrUpdate(mockMovieDetails)
            Mockito.verify(mockCachesLifeManager, Mockito.times(1)).generateMovieDetailsCache(mockMovieId)
        }
    }

    @Test
    fun getMovieDetails_getFromRemote_Failure() {
        scope.launch {
            val mockMovieId = 123
            val mockMovievideo = MovieVideo()
            val mockMovieSummary = MovieSummary()
            val mockMovieDetails = MovieDetails()

            Mockito.`when`(mockCachesLifeManager.movieCacheIsValid(mockMovieId))
                .thenReturn(false)
            Mockito.`when`(mockApiDataSource.getMovieVideos(mockMovieId)).thenReturn(mockMovievideo)
            Mockito.`when`(mockApiDataSource.getMovieDetails(mockMovieId)).thenThrow(Exception())
            Mockito.`when`(mockMapper.map(mockMovieSummary)).thenReturn(mockMovieDetails)

            testSubject.getMovieDetails(mockMovieId)

            Mockito.verify(mockMovieDetailsDao, Mockito.times(0)).createOrUpdate(mockMovieDetails)
            Mockito.verify(mockCachesLifeManager, Mockito.times(0)).generateMovieDetailsCache(mockMovieId)
        }
    }

    @Test
    fun getMovieDetails_getFromLocal_Success() {
        scope.launch {
            val mockMovieId = 123
            val mockMovieDetails = MovieDetails()

            Mockito.`when`(mockCachesLifeManager.movieCacheIsValid(mockMovieId))
                .thenReturn(true)

            Mockito.`when`(mockMovieDetailsDao.read(mockMovieId)).thenReturn(listOf(mockMovieDetails))
            Mockito.verify(mockMovieDetailsDao, Mockito.times(1)).read(mockMovieId)
        }
    }

    @Test
    fun getMovieDetails_getFromLocal_Failure() {
        scope.launch {
            val mockMovieId = 123
            val mockMovieDetails = MovieDetails()

            Mockito.`when`(mockCachesLifeManager.movieCacheIsValid(mockMovieId))
                .thenReturn(true)

            Mockito.`when`(mockMovieDetailsDao.read(mockMovieId)).thenThrow(Exception())
            Mockito.verify(mockMovieDetailsDao, Mockito.times(1)).read(mockMovieId)
        }
    }
}