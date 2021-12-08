package me.alfredobejarano.movieslist.remote.map

import me.alfredobejarano.movieslist.remote.model.MovieResult
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RunWith(JUnit4::class)
class MovieResultsMapperTest {
    private val movieResult = MovieResult()
    private val mockBaseImageURL = "test"
    private val testSubject = MovieResultMapper(mockBaseImageURL)
    private val todayDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())

    @Test
    fun mapMockMovieSummary_thenReturnMovieDetails() {
        val resultMovie = testSubject.map(movieResult)

        assert(resultMovie.id == 0)
        assert(resultMovie.title == "")
        assert(resultMovie.rating == "0%")
        assert(resultMovie.releaseDate == todayDate)
        assert(resultMovie.posterURL == mockBaseImageURL)
    }
}