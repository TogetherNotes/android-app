package com.example.togethernotes.api

import com.example.togethernotes.models.App
import retrofit2.Response
import retrofit2.http.*

interface AppService {

    // GET: api/apps
    @GET("api/apps")
    suspend fun getAllApps(): Response<List<App>>

    // GET: api/apps/{id}
    @GET("api/apps/{id}")
    suspend fun getAppById(@Path("id") id: Int): Response<App>

    // PUT: api/apps/{id}
    @PUT("api/apps/{id}")
    suspend fun updateApp(@Path("id") id: Int, @Body app: App): Response<Any>

    // POST: api/apps/login
    @POST("api/apps/login")
    suspend fun login(@Body creds: App): Response<App>
}