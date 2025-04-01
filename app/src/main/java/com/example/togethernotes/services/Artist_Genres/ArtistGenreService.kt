package com.example.togethernotes.api

import com.example.togethernotes.models.ArtistGenre
import retrofit2.Response
import retrofit2.http.*

interface ArtistGenreService {

    // GET: api/artist_genres
    @GET("api/artist_genres")
    suspend fun getAllArtistGenres(): Response<List<ArtistGenre>>

    // GET: api/artist_genres/{genre_id}
    @GET("api/artist_genres/{id}")
    suspend fun getArtistGenresByGenreId(@Path("id") genreId: Int): Response<List<ArtistGenre>>

    // PUT: api/artist_genres/{artist_id}
    @PUT("api/artist_genres/{id}")
    suspend fun updateArtistGenre(
        @Path("id") artistId: Int,
        @Body artistGenre: ArtistGenre
                                 ): Response<Any>

    // POST: api/artist_genres
    @POST("api/artist_genres")
    suspend fun addArtistGenre(@Body artistGenre: ArtistGenre): Response<ArtistGenre>

    // DELETE: api/artist_genres/{artist_id}
    @DELETE("api/artist_genres/{id}")
    suspend fun deleteArtistGenre(@Path("id") artistId: Int): Response<Any>
}