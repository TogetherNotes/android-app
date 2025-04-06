package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.TempMatch
import com.example.togethernotes.models.TempMatchDto
import com.example.togethernotes.services.tempmatch.TempMatchService
import retrofit2.Response

class TempMatchRepository {

    private val tempMatchService: TempMatchService by lazy {
        Retrofit.createService(TempMatchService::class.java)
    }

    // Obtener todos los registros de temp_match
    suspend fun getAllTempMatches(): Response<List<TempMatch>> {
        return tempMatchService.getAllTempMatches()
    }

    // Obtener un registro específico por artist_id y space_id
    suspend fun getTempMatchById(artistId: Int, spaceId: Int): Response<TempMatch> {
        return tempMatchService.getTempMatchById(artistId, spaceId)
    }

    // Actualizar un registro existente
    suspend fun updateTempMatch(artistId: Int, spaceId: Int, tempMatch: TempMatch): Response<Void> {
        return tempMatchService.updateTempMatch(artistId, spaceId, tempMatch)
    }

    // Crear un nuevo registro
    suspend fun createTempMatch(tempMatch: TempMatch): Response<TempMatch> {
        return tempMatchService.createTempMatch(tempMatch)
    }

    // Eliminar un registro
    suspend fun deleteTempMatch(artistId: Int, spaceId: Int): Response<TempMatch> {
        return tempMatchService.deleteTempMatch(artistId, spaceId)
    }
    // Obtener coincidencias pendientes para un usuario específico
    suspend fun getPendingMatches(userId: Int): Response<List<TempMatchDto>> {
        return tempMatchService.getPendingMatches(userId)
    }
}