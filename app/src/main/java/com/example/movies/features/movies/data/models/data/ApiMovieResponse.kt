package com.example.movies.features.movies.data.models.data

data class ApiMovieResponse(
    val page: Int,
    val results: MutableList<MoviesResponse>,
    val total_pages: Int,
    val total_results: Int
)