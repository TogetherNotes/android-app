package com.example.togethernotes.repository

import com.example.togethernotes.Retrofit
import com.example.togethernotes.models.Contract
import com.example.togethernotes.services.contract.ContractService
import retrofit2.Response

class ContractRepository {

    private val contractService: ContractService by lazy {
        Retrofit.createService(ContractService::class.java)
    }

    suspend fun getAllContracts(): Response<List<Contract>> {
        return contractService.getAllContracts()
    }

    suspend fun getContractByArtistAndSpaceId(artistId: Int, spaceId: Int): Response<Contract?> {
        return contractService.getContractByArtistAndSpaceId(artistId, spaceId)
    }

    suspend fun updateContract(artistId: Int, spaceId: Int, contractData: Contract): Response<Void> {
        return contractService.updateContract(artistId, spaceId, contractData)
    }

    suspend fun createContract(contract: Contract): Response<Contract> {
        return contractService.createContract(contract)
    }

    suspend fun deleteContract(artistId: Int, spaceId: Int, initHour: String, endHour: String): Response<Contract> {
        return contractService.deleteContract(artistId, spaceId, initHour, endHour)
    }
}