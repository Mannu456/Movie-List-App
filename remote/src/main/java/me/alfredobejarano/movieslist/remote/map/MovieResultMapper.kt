package me.alfredobejarano.movieslist.remote.map

import me.alfredobejarano.movieslist.core.Movie
import me.alfredobejarano.movieslist.remote.model.MovieResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by alfredo on 2019-08-02.
 */
class MovieResultMapper(private val baseImageURL: String) : Mapper<MovieResult, Movie> {
    companion object {
        private const val RELEASE_DATE_UI_FORMAT = "MMMM dd, yyyy"
        private const val RELEASE_DATE_REMOTE_FORMAT = "yyyy-MM-dd"
    }

    override fun map(remote: MovieResult) = Movie(
        id = remote.id ?: 0,
        title = remote.title ?: "",
        posterURL = "$baseImageURL${remote.posterPath}",
        rating = getRatingPercentage(remote.voteAverage),
        releaseDate = getUIReleaseDate(remote.releaseDate)
    )

    /**
     * The vote average value comes in a double that scale spans
     * from 0.0 to 10.0
     */
    private fun getRatingPercentage(voteAverage: Double?) = "${((voteAverage?.times(100.0) ?: 0.0) / 10.0).toInt()}%"

    /**
     * Parses the remote date format into a human-readable value.
     */
    private fun getUIReleaseDate(releaseDate: String?): String = synchronized(this) {
        val uiFormatter = SimpleDateFormat(RELEASE_DATE_UI_FORMAT, Locale.getDefault())

        return try {
            val date = SimpleDateFormat(RELEASE_DATE_REMOTE_FORMAT, Locale.getDefault())
                .parse(releaseDate ?: "2000-01-01") ?: Date()

            uiFormatter.format(date)
        } catch (e: Exception) {
            uiFormatter.format(Date())
        }
    }
}