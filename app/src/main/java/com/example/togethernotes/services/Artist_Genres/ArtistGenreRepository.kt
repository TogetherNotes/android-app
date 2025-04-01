package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.api.ArtistGenreService
import com.example.togethernotes.models.ArtistGenre
import retrofit2.Response

class ArtistGenreRepository {

    private val artistGenreService: ArtistGenreService by lazy {
        Retrofit.createService(ArtistGenreService::class.java)
    }

    suspend fun getAllArtistGenres(): Response<List<ArtistGenre>> {
        return artistGenreService.getAllArtistGenres()
    }

    suspend fun getArtistGenresByGenreId(genreId: Int): Response<List<ArtistGenre>> {
        return artistGenreService.getArtistGenresByGenreId(genreId)
    }

    suspend fun updateArtistGenre(artistId: Int, artistGenre: ArtistGenre): Response<Any> {
        return artistGenreService.updateArtistGenre(artistId, artistGenre)
    }

    suspend fun addArtistGenre(artistGenre: ArtistGenre): Response<ArtistGenre> {
        return artistGenreService.addArtistGenre(artistGenre)
    }

    suspend fun deleteArtistGenre(artistId: Int): Response<Any> {
        return artistGenreService.deleteArtistGenre(artistId)
    }
}