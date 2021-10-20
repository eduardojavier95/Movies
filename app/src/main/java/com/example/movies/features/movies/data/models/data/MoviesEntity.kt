package com.example.movies.features.movies.data.models.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "MoviesEntity")
data class MoviesEntity(
    val overview: String,
    val release_date: String,
    val title: String,
    val adult: Boolean,
    val backdrop_path: String,
//    val genre_ids: List<Int>,
    val vote_count: Int,
    val original_language: String,
    val original_title: String,
    val poster_path: String,
    @PrimaryKey val id: Long,
    val video: Boolean,
    val vote_average: Float,
    val popularity: Float,
    val media_type: String
)