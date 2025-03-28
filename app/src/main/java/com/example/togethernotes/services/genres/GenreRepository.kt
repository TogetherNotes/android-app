package com.example.togethernotes.services.genres

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.Genres
import retrofit2.Response

class GenreRepository {

    private val genresService: GenresService = Retrofit.createService(GenresService::class.java)

    suspend fun getAllGenres(): Response<List<Genres>> {
        return genresService.getAllGenres()
    }
    /*
    suspend fun getGenreById(id: Int): Response<Genre> {
        return GenresService.getGenreById(id)
    }

    suspend fun createGenre(genre: Genre): Response<Genre> {
        return GenresService.createGenre(genre)
    }

    suspend fun updateGenre(id: Int, genre: Genre): Response<Void> {
        return GenresService.updateGenre(id, genre)
    }

    suspend fun deleteGenre(id: Int): Response<Void> {
        return GenresService.deleteGenre(id)
    }

     */
}