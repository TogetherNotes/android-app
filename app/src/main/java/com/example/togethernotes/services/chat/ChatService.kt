package com.example.togethernotes.services.chat

import com.example.togethernotes.models.Chat
import retrofit2.Response
import retrofit2.http.*

interface ChatService {

    // GET: api/chats
    @GET("api/chats")
    suspend fun getAllChats(): Response<List<Chat>>

    // GET: api/chats/{id}
    @GET("api/chats/{id}")
    suspend fun getChatById(@Path("id") id: Int): Response<Chat>

    // GET: api/chats/user/{id}
    @GET("api/chats/user/{id}")
    suspend fun getChatsByUserId(@Path("id") userId: Int): Response<List<Chat>>

    // PUT: api/chats/{id}
    @PUT("api/chats/{id}")
    suspend fun updateChat(@Path("id") id: Int, @Body chat: Chat): Response<Void>

    // POST: api/chats
    @POST("api/chats")
    suspend fun createChat(@Body chat: Chat): Response<Chat>

    // DELETE: api/chats/{id}
    @DELETE("api/chats/{id}")
    suspend fun deleteChat(@Path("id") id: Int): Response<Chat>
}
