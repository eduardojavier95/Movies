package com.example.movies.features.movies.data.services

import com.example.movies.features.movies.data.models.data.ApiMovieResponse
import com.example.movies.features.movies.data.models.data.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getMovies(@Url url: String): Response<ApiMovieResponse>
}