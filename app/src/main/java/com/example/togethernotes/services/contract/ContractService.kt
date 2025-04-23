package com.example.togethernotes.services.contract

import com.example.togethernotes.models.Contract
import retrofit2.Response
import retrofit2.http.*

interface ContractService {

    // GET: api/contracts
    @GET("api/contracts")
    suspend fun getAllContracts(): Response<List<Contract>>

    // GET: api/contracts/{artist_id}/{space_id}
    @GET("api/contracts/{artist_id}/{space_id}")
    suspend fun getContractByArtistAndSpaceId(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int
    ): Response<Contract?>

    // PUT: api/contracts/{artist_id}/{space_id}
    @PUT("api/contracts/{artist_id}/{space_id}")
    suspend fun updateContract(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int,
        @Body contractData: Contract
    ): Response<Void>

    // POST: api/contracts
    @POST("api/contracts")
    suspend fun createContract(@Body contract: Contract): Response<Contract>

    // DELETE: api/contracts/{artist_id}/{space_id}/{init_hour}/{end_hour}
    @DELETE("api/contracts/{artist_id}/{space_id}/{init_hour}/{end_hour}")
    suspend fun deleteContract(
        @Path("artist_id") artistId: Int,
        @Path("space_id") spaceId: Int,
        @Path("init_hour") initHour: String,
        @Path("end_hour") endHour: String
    ): Response<Contract>

    @GET("api/contracts/by-date")
    suspend fun getContractsByDate(
        @Query("userId") userId: Int,
        @Query("date") date: String // Asumimos que la fecha se envía como String en formato ISO 8601 (YYYY-MM-DD)
      ): Response<List<Contract>>
}