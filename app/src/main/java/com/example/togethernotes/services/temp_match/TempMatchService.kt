package com.example.togethernotes.services.tempmatch

import com.example.togethernotes.models.TempMatch
import com.example.togethernotes.models.TempMatchDto
import retrofit2.Response
import retrofit2.http.*

interface TempMatchService {

    // GET: api/temp_match
    @GET("api/temp_match")
    suspend fun getAllTempMatches(): Response<List<TempMatch>>

    // GET: api/temp_match/{artist_id}/{space_id}
    @GET("api/temp_match/{artist_id}/{space_id}")
    suspend fun getTempMatchById(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int
    ): Response<TempMatch>

    // PUT: api/temp_match/{artist_id}/{space_id}
    @PUT("api/temp_match/{artist_id}/{space_id}")
    suspend fun updateTempMatch(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int,
        @Body tempMatch: TempMatch
    ): Response<Void>

    // POST: api/temp_match
    @POST("api/temp_match")
    suspend fun createTempMatch(@Body tempMatch: TempMatch): Response<TempMatch>

    // DELETE: api/temp_match/{artist_id}/{space_id}
    @DELETE("api/temp_match/{artist_id}/{space_id}")
    suspend fun deleteTempMatch(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int
    ): Response<TempMatch>
    // GET: api/temp_match/pending/{userId}
    @GET("api/temp_match/pending")
    suspend fun getPendingMatches(@Query("userId") userId: Int): Response<List<TempMatchDto>>
}