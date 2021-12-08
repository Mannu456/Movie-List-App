package me.alfredobejarano.movieslist.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetails(
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val title: String = "",
    val runtime: Int = 0,
    val votesCount: Int = 0,
    val genres: List<String> = emptyList(),
    val posterURL: String = "",
    val overview: String = "",
    val videoKey: String = ""
)