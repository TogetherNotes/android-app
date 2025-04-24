package com.example.togethernotes.services.files

import com.example.togethernotes.Retrofit
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

class FileRepository {

    private val fileService: FileService by lazy {
        Retrofit.createService(FileService::class.java)
    }

    suspend fun uploadFile(appId: Int, file: MultipartBody.Part): Response<ResponseBody> {
        return fileService.uploadFile(appId, file)
    }

    suspend fun downloadFile(appId: Int, type: String): Response<ResponseBody> {
        return fileService.downloadFile(appId, type)
    }
}
