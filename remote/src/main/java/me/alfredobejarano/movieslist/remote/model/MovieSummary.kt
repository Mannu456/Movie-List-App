package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieSummary(
    @Expose
    @SerializedName("id")
    val id: Int? = 0,
    @Expose
    @SerializedName("title")
    val title: String? = "",
    @Expose
    @SerializedName("runtime")
    val runtime: Int? = 0,
    @Expose
    @SerializedName("vote_count")
    val votesCount: Int? = 0,
    @Expose
    @SerializedName("genres")
    val genres: List<MovieGenre>? = emptyList(),
    @Expose
    @SerializedName("poster_path")
    val poster: String? = "",
    @Expose
    @SerializedName("overview")
    val overview: String? = ""
)