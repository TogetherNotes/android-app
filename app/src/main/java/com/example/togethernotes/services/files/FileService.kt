package com.example.togethernotes.services.files

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface FileService {

    // POST: api/files/upload/{app_id}
    @Multipart
    @POST("api/files/upload/{app_id}")
    suspend fun uploadFile(
        @Path("app_id") appId: Int,
        @Part file: MultipartBody.Part
                          ): Response<ResponseBody>

    // GET: api/files/download/{app_id}/{type}
    @GET("api/files/download/{app_id}/{type}")
    suspend fun downloadFile(
        @Path("app_id") appId: Int,
        @Path("type") type: String // "image" o "audio"
                            ): Response<ResponseBody>
}
