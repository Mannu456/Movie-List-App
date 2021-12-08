package me.alfredobejarano.local.dao

import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.movieslist.core.MovieDetails
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
class MovieDetailsDaoTest {
    private val scope = GlobalScope
    private lateinit var testInMemoryDB: AppDatabase
    private lateinit var testCandidate: MovieDetailsDao

    @Before
    fun setup() {
        testInMemoryDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDatabase::class.java).build()
        testCandidate = testInMemoryDB.provideMovieDetailsDao()
    }

    @Test
    fun createTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = MovieDetails(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @Test
    fun updateTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = MovieDetails(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)
            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject.title == "")

            val updatedTitle = "title"
            val updateTestSubject = MovieDetails(id = testSubjectId, title = updatedTitle)
            testCandidate.createOrUpdate(updateTestSubject)

            assert(retrievedSubject.title == updatedTitle)
        }
    }

    @Test
    fun readTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = MovieDetails(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}