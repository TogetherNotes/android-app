package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.Match
import com.example.togethernotes.services.matches.MatchService
import retrofit2.Response

class MatchRepository {

    private val matchesService: MatchService by lazy {
        Retrofit.createService(MatchService::class.java)
    }

    // Obtener todos los matches
    suspend fun getAllMatches(): Response<List<Match>> {
        return matchesService.getAllMatches()
    }

    // Obtener un match específico por artist_id y space_id
    suspend fun getMatchById(artistId: Int, spaceId: Int): Response<Match> {
        return matchesService.getMatchById(artistId, spaceId)
    }

    // Crear un nuevo match
    suspend fun createMatch(match: Match): Response<Match> {
        return matchesService.createMatch(match)
    }

    // Actualizar un match existente
    suspend fun updateMatch(artistId: Int, spaceId: Int, updatedMatch: Match): Response<Void> {
        return matchesService.updateMatch(artistId, spaceId, updatedMatch)
    }

    // Eliminar un match
    suspend fun deleteMatch(artistId: Int, spaceId: Int): Response<Void> {
        return matchesService.deleteMatch(artistId, spaceId)
    }
}