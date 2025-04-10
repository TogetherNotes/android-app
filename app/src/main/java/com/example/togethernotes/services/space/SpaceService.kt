package com.example.togethernotes.services.space

import com.example.togethernotes.models.RegisterResponse
import com.example.togethernotes.models.Space
import retrofit2.Response
import retrofit2.http.*

interface SpaceService {

    // GET: api/spaces
    @GET("api/spaces")
    suspend fun getAllSpaces(): Response<List<Space>>

    // GET: api/spaces/{id}
    @GET("api/spaces/{id}")
    suspend fun getSpaceById(@Path("id") id: Int): Response<Space>

    // PUT: api/spaces/{id}
    @PUT("api/spaces/{id}")
    suspend fun updateSpace(
        @Path("id") id: Int,
        @Body space: Space
                           ): Response<Void>

    // POST: api/spaces/register
    @POST("api/spaces/register")
    suspend fun registerSpace(@Body request: Space): Response<RegisterResponse>
}