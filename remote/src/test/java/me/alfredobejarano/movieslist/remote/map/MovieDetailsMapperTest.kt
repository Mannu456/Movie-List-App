package me.alfredobejarano.movieslist.remote.map

import me.alfredobejarano.movieslist.remote.model.MovieSummary
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieDetailsMapperTest {
    private val movieSummary = MovieSummary()
    private val mockBaseImageURL = "test"
    private val testSubject: MovieDetailsMapper = MovieDetailsMapper(mockBaseImageURL)

    @Test
    fun mapMockMovieSummary_thenReturnMovieDetails() {
        testSubject.movieVideoKey = "mockVideoKey"
        val resultMovie = testSubject.map(movieSummary)

        assert(resultMovie.id == 0)
        assert(resultMovie.title == "")
        assert(resultMovie.runtime == 0)
        assert(resultMovie.overview == "")
        assert(resultMovie.votesCount == 0)
        assert(resultMovie.genres.isEmpty())
        assert(resultMovie.posterURL == mockBaseImageURL)
        assert(resultMovie.videoKey == testSubject.movieVideoKey)
    }
}