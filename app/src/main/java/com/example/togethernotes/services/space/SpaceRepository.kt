package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.Space
import com.example.togethernotes.services.space.SpaceService
import retrofit2.Response

class SpaceRepository {

    private val spaceService: SpaceService by lazy {
        Retrofit.createService(SpaceService::class.java)
    }

    suspend fun getAllSpaces(): Response<List<Space>> {
        return spaceService.getAllSpaces()
    }

    suspend fun getSpaceById(id: Int): Response<Space> {
        return spaceService.getSpaceById(id)
    }
    /*
    suspend fun updateSpace(id: Int, space: Space): Response<Any> {
        return spaceService.updateSpace(id, space)
    }
    */

    suspend fun registerSpace(data: Space): Response<Space> {
        return spaceService.registerSpace(data)
    }


}