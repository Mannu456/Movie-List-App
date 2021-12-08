package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieVideo(
    @Expose
    @SerializedName("id")
    val id: Int? = 0,
    @Expose
    @SerializedName("results")
    val videoResults: List<MovieVideoResult>? = emptyList()
)