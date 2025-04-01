package com.example.togethernotes.services.genres

import com.example.togethernotes.models.Genres
import retrofit2.Response
import retrofit2.http.*
import java.util.Objects

interface GenresService {

    // GET: api/Genre
    @GET("api/genres")
    suspend fun getAllGenres(): Response<List<Genres>>

    // GET: api/Genres/{id}
    @GET("api/genres/{id}")
    suspend fun getGenresById(@Path("id") id: Int): Response<Genres>

    // POST: api/Genress
    @POST("api/genres")
    suspend fun createGenres(@Body Genres: Genres): Response<Genres>

    // PUT: api/Genress/{id}
    @PUT("api/genres/{id}")
    suspend fun updateGenres(
        @Path("id") id: Int,
        @Body Genres: Genres
                           ): Response<Void>

    // DELETE: api/Genress/{id}
    @DELETE("api/Genress/{id}")
    suspend fun deleteGenres(@Path("id") id: Int): Response<Void>
}