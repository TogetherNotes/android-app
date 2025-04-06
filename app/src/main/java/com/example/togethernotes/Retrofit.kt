package com.example.togethernotes

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object Retrofit {
    private const val BASE_URL = "http://192.168.1.133/WebApplicationTgtNotes/"

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder()

            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}