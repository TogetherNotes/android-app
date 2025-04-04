package com.example.togethernotes.repository

import Message
import com.example.togethernotes.Retrofit
import com.example.togethernotes.services.messages.MessageService
import retrofit2.Response

class MessageRepository {

    private val messageService: MessageService by lazy {
        Retrofit.createService(MessageService::class.java)
    }

    suspend fun getAllMessages(): Response<List<Message>> {
        return messageService.getMessages()
    }

    suspend fun getMessageById(id: Int): Response<Message> {
        return messageService.getMessageById(id)
    }

    suspend fun getMessagesByChat(chatId: Int): Response<List<Message>> {
        return messageService.getMessagesByChat(chatId)
    }

    suspend fun sendMessage(message: Message): Response<Message> {
        return messageService.sendMessage(message)
    }

    suspend fun markMessageAsRead(id: Int, message: Message): Response<Message> {
        return messageService.markMessageAsRead(id, message)
    }

    suspend fun deleteMessage(id: Int): Response<Void> {
        return messageService.deleteMessage(id)
    }
}
