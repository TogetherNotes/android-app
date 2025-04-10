package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.api.ArtistService
import com.example.togethernotes.models.Artist
import com.example.togethernotes.models.RegisterResponse
import retrofit2.Response

class ArtistRepository {

    private val artistService: ArtistService by lazy {
        Retrofit.createService(ArtistService::class.java)
    }

    suspend fun getAllArtists(): Response<List<Artist>> {
        return artistService.getAllArtists()
    }

    suspend fun getArtistById(id: Int): Response<Artist> {
        return artistService.getArtistById(id)
    }

    suspend fun updateArtist(id: Int, artist: Artist): Response<Any> {
        return artistService.updateArtist(id, artist)
    }

    suspend fun registerArtist(data: Artist): Response<RegisterResponse> {
        return artistService.registerArtist(data)
    }
}