package me.alfredobejarano.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Defines the list of ids for a movie type.
 *
 * Created by alfredo on 2019-08-02.
 */
@Entity(tableName = "movie_list_index")
data class MovieListIndex(
    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val movies: List<Int> = emptyList()
)