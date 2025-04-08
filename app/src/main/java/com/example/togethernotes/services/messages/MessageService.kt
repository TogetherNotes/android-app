package com.example.togethernotes.services.messages

import Message
import retrofit2.Response
import retrofit2.http.*

interface MessageService {

    // GET: api/messages
    @GET("api/messages")
    suspend fun getMessages(): Response<List<Message>>

    // GET: api/messages/{id}
    @GET("api/messages/{id}")
    suspend fun getMessageById(@Path("id") id: Int): Response<Message>

    // GET: api/messages?chat_id={chatId}
    @GET("api/messages")
    suspend fun getMessagesByChat(@Query("chat_id") chatId: Int): Response<List<Message>>

    // POST: api/messages
    @POST("api/messages")
    suspend fun sendMessage(@Body message: Message): Response<Message>

    // PUT: api/messages/{id}/read
    @PUT("api/messages/{id}")
    suspend fun markMessageAsRead(@Path("id") id: Int, @Body message: Message): Response<Message>

    // DELETE: api/messages/{id}
    @DELETE("api/messages/{id}")
    suspend fun deleteMessage(@Path("id") id: Int): Response<Void>
}
