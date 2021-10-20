package com.example.movies.features.movies.data.models.database


import androidx.room.*
import com.example.movies.features.movies.data.models.data.MoviesEntity

@Dao
interface MoviesDao {
    @Query("SELECT * FROM MoviesEntity")
    fun getAllMovies(): MutableList<MoviesEntity>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun addMovies(moviesEntity: MoviesEntity)

    @Update
    fun updateMovies(moviesEntity: MoviesEntity)

    @Delete
    fun deleteMovies(moviesEntity: MoviesEntity)

    @Query("SELECT * FROM MoviesEntity WHERE id = :id")
    fun findMovies(id: Long): MoviesEntity

}