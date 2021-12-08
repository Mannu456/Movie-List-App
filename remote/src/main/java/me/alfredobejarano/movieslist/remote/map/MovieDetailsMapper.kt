package me.alfredobejarano.movieslist.remote.map

import me.alfredobejarano.movieslist.core.MovieDetails
import me.alfredobejarano.movieslist.remote.model.MovieGenre
import me.alfredobejarano.movieslist.remote.model.MovieSummary

class MovieDetailsMapper(private val baseImageURL: String) : Mapper<MovieSummary, MovieDetails> {
    var movieVideoKey = ""

    override fun map(remote: MovieSummary) = MovieDetails(
        remote.id ?: 0,
        remote.title ?: "",
        remote.runtime ?: 0,
        remote.votesCount ?: 0,
        extractGenresNames(remote.genres ?: emptyList()),
        "$baseImageURL${remote.poster}",
        remote.overview ?: "",
        movieVideoKey
    )

    private fun extractGenresNames(list: List<MovieGenre>) = list.map {
        it.name ?: ""
    }
}