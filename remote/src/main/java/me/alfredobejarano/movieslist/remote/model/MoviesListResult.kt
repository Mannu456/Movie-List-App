package me.alfredobejarano.movieslist.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alfredo on 2019-08-02.
 */
data class MoviesListResult(
    @Expose
    @SerializedName("page")
    val page: Int? = 0,
    @Expose
    @SerializedName("total_pages")
    val totalPages: Int? = 0,
    @Expose
    @SerializedName("total_results")
    val totalResults: Int? = 0,
    @Expose
    @SerializedName("results")
    val results: List<MovieResult>? = emptyList()
)