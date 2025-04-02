package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.Space
import com.example.togethernotes.services.space.SpaceService
import retrofit2.Response

class SpaceRepository {

    private val spaceService: SpaceService by lazy {
        Retrofit.createService(SpaceService::class.java)
    }

    // Obtener todos los espacios
    suspend fun getAllSpaces(): Response<List<Space>> {
        return spaceService.getAllSpaces()
    }

    // Obtener un espacio por ID
    suspend fun getSpaceById(id: Int): Response<Space> {
        return spaceService.getSpaceById(id)
    }

    // Actualizar un espacio
    /*
    suspend fun updateSpace(id: Int, space: Space): Response<Any> {
        return spaceService.updateSpace(id, space)
    }
    */
    // Registrar un nuevo espacio
    suspend fun registerSpace(data: Space): Response<Map<String, Any>> {
        return spaceService.registerSpace(data)
    }
}