package com.example.togethernotes.services.matches

import com.example.togethernotes.models.Match
import retrofit2.Response
import retrofit2.http.*

interface MatchService {

    // GET: api/matches
    @GET("api/matches")
    suspend fun getAllMatches(): Response<List<Match>>

    // GET: api/matches/{artist_id}/{space_id}
    @GET("api/matches/{artist_id}/{space_id}")
    suspend fun getMatchById(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int
                            ): Response<Match>

    // POST: api/matches
    @POST("api/matches")
    suspend fun createMatch(@Body match: Match): Response<Match>

    // PUT: api/matches/{artist_id}/{space_id}
    @PUT("api/matches/{artist_id}/{space_id}")
    suspend fun updateMatch(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int,
        @Body match: Match
                           ): Response<Void>

    // DELETE: api/matches/{artist_id}/{space_id}
    @DELETE("api/matches/{artist_id}/{space_id}")
    suspend fun deleteMatch(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int
                           ): Response<Void>
}