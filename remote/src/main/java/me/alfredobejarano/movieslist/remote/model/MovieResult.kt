package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model data class defining a Movie from TheMovieDB API.
 *
 * Values in this data class are defined as _nullable_ and
 * _optional_ because when the network data is not reliable and
 * the values are defined as non-null, they can be received as null
 * (because an incomplete JSON, for example), it can cause a
 * Kotlin.NullPointerException and we don't want that because we are writting
 * Kotlin.
 *
 * Created by alfredo on 2019-08-02.
 */
data class MovieResult(
    @Expose
    @SerializedName("id")
    val id: Int? = 0,
    @Expose
    @SerializedName("title")
    val title: String? = "",
    @Expose
    @SerializedName("poster_path")
    val posterPath: String? = "",
    @Expose
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0,
    @Expose
    @SerializedName("release_date")
    val releaseDate: String? = ""
)