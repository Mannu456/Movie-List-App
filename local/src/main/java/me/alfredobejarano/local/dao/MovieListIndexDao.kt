package me.alfredobejarano.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.alfredobejarano.local.entity.MovieListIndex
import me.alfredobejarano.movieslist.core.MovieListType

/**
 * Created by alfredo on 2019-08-02.
 */
@Dao
interface MovieListIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(movieIndex: MovieListIndex)

    /**
     * Reads the index of a list type.
     * The integer is retrieved from the [MovieListType] enum class ordinal value.
     */
    @Query("SELECT * FROM movie_list_index WHERE pk == :type")
    suspend fun getListIndex(type: Int): List<MovieListIndex>

    @Delete
    suspend fun delete(movieIndex: MovieListIndex)
}