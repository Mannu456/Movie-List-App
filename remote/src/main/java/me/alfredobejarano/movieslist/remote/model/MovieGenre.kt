package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieGenre(
    @Expose
    @SerializedName("name")
    val name: String? = ""
)