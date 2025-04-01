package com.example.togethernotes.api

import com.example.togethernotes.models.Artist
import retrofit2.Response
import retrofit2.http.*

interface ArtistService {

    // GET: api/artists
    @GET("api/artists")
    suspend fun getAllArtists(): Response<List<Artist>>

    // GET: api/artists/{id}
    @GET("api/artists/{id}")
    suspend fun getArtistById(@Path("id") id: Int): Response<Artist>

    // PUT: api/artists/{id}
    @PUT("api/artists/{id}")
    suspend fun updateArtist(
        @Path("id") id: Int,
        @Body artist: Artist
                            ): Response<Any>

    // POST: api/artists/register
    @POST("api/artists/register")
    suspend fun registerArtist(@Body data: Artist): Response<Artist>
}