package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.api.AppService
import com.example.togethernotes.models.App
import retrofit2.Response

class AppRepository {

    private val appService: AppService by lazy {
        Retrofit.createService(AppService::class.java)
    }

    suspend fun getAllApps(): Response<List<App>> {
        return appService.getAllApps()
    }

    suspend fun getAppById(id: Int): Response<App> {
        return appService.getAppById(id)
    }

    suspend fun updateApp(id: Int, app: App): Response<Any> {
        return appService.updateApp(id, app)
    }

    suspend fun login(mail: String, password: String): Response<App> {
        return appService.login(mail,password)
    }
    suspend fun getAppsByLocation(lat: Double, lng: Double, radiusKm: Double): Response<List<App>> {
        return appService.getAppsByLocation(lat, lng, radiusKm)
    }
}