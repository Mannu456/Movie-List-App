package me.alfredobejarano.local.dao

import android.os.Build
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.alfredobejarano.local.AppDatabase
import me.alfredobejarano.movieslist.core.Movie
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
class MovieDaoTest {
    private val scope = GlobalScope
    private lateinit var testCandidate: MovieDao
    private lateinit var testInMemoryDB: AppDatabase

    @Before
    fun setup() {
        testInMemoryDB = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, AppDatabase::class.java).build()
        testCandidate = testInMemoryDB.provideMovieDao()
    }

    @Test
    fun createTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = Movie(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @Test
    fun updateTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = Movie(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)
            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject.title == "")

            val updatedTitle = "title"
            val updateTestSubject = Movie(id = testSubjectId, title = updatedTitle)
            testCandidate.createOrUpdate(updateTestSubject)

            assert(retrievedSubject.title == updatedTitle)
        }
    }

    @Test
    fun readTest() {
        scope.launch {
            val testSubjectId = Random.nextInt()
            val testSubject = Movie(id = testSubjectId)

            testCandidate.createOrUpdate(testSubject)

            val retrievedSubject = testCandidate.read(testSubjectId).first()

            assert(retrievedSubject == testSubject)
        }
    }

    @Test
    fun findByTitleTest() {
        scope.launch {
            val testSubject = Movie(title = "My awesome movie")

            testCandidate.createOrUpdate(testSubject)

            var retrievedSubjects = testCandidate.findByTitle("awes")
            assert(retrievedSubjects.isNotEmpty())

            retrievedSubjects = testCandidate.findByTitle("ewe")
            assert(retrievedSubjects.isEmpty())
        }
    }

    @Test
    fun deleteTest() {
        scope.launch {
            val testSubject = Movie()
            testCandidate.createOrUpdate(testSubject)

            var movies = testCandidate.read(0)
            assert(movies.isNotEmpty())

            testCandidate.delete(movies.first())
            movies = testCandidate.read(0)

            assert(movies.isEmpty())
        }
    }

    @Test
    fun deleteAllTest() {
        scope.launch {
            val testSubject = Movie()
            testCandidate.createOrUpdate(testSubject)

            var movies = testCandidate.read(0)
            assert(movies.isNotEmpty())

            testCandidate.deleteAll()
            movies = testCandidate.read(0)

            assert(movies.isEmpty())
        }
    }

    @After
    fun tearDown() = testInMemoryDB.close()
}