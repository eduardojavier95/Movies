package com.example.movies

import android.app.Application
import androidx.room.Room
import com.example.movies.features.movies.data.models.database.MoviesDatabase

class MovieAplication: Application() {
    companion object{
        //lateinit var storeAPI: StoreAPI
        lateinit var database: MoviesDatabase
    }
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            MoviesDatabase::class.java,
            "MoviesDatabase")
            .build()
    }
}