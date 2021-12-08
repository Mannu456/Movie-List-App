package me.alfredobejarano.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.alfredobejarano.movieslist.core.Movie

/**
 * Created by alfredo on 2019-08-02.
 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(movie: Movie)

    @Query("SELECT * FROM movies WHERE pk == :movieId")
    suspend fun read(movieId: Int): List<Movie>

    @Query("SELECT * FROM movies WHERE title LIKE :query")
    suspend fun findByTitle(query: String): List<Movie>

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM movies")
    suspend fun deleteAll()
}