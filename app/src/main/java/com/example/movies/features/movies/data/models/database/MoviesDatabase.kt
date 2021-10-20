package com.example.movies.features.movies.data.models.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movies.features.movies.data.models.data.MoviesEntity


@Database(entities = arrayOf(MoviesEntity::class),version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao():MoviesDao
}