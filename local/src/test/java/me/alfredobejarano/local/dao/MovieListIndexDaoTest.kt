package me.alfredobejarano.local.dao

import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.movieslist.core.MovieListType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.random.Random

@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP])
@RunWith(RobolectricTestRunner::class)
class MovieListIndexDaoTest {
    private val scope = GlobalScope
    private lateinit var testInMemoryDB: AppDatabase
    private lateinit var testCandidate: MovieListIndexDao

    @Before
    fun setup() {
        testInMemoryDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDatabase::class.java).build()
        testCandidate = testInMemoryDB.provideMovieListIndexDao()
    }

    @Test
    fun createTest() {
        scope.launch {
            val id = MovieListType.MOVIE_LIST_POPULAR.ordinal
            val testSubject = MovieListIndex(id = id)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.getListIndex(id).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @Test
    fun updateTest() {
        scope.launch {
            val testSubjectId = MovieListType.MOVIE_LIST_POPULAR.ordinal
            val testSubject = MovieListIndex(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)
            val retrievedSubject = testCandidate.getListIndex(testSubjectId).first()

            assert(retrievedSubject.movies.isEmpty())

            val updatedMovies = listOf(1, 2, 3)
            val updateTestSubject = MovieListIndex(id = testSubjectId, movies = updatedMovies)
            testCandidate.createOrUpdate(updateTestSubject)

            assert(retrievedSubject.movies.isNotEmpty())
        }
    }

    @Test
    fun getListIndexTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = MovieListIndex(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.getListIndex(testSubjectId).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @Test
    fun deleteTest() {
        scope.launch {
            val id = MovieListType.MOVIE_LIST_POPULAR.ordinal
            val testSubject = MovieListIndex(id = id)

            testCandidate.createOrUpdate(testSubject)

            var index = testCandidate.getListIndex(id)
            assert(index.isNotEmpty())

            testCandidate.delete(testSubject)
            index = testCandidate.getListIndex(id)
            assert(index.isEmpty())
        }
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}