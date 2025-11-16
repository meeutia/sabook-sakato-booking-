package com.example.sebook.data.repository

import com.example.sebook.data.model.HistoryResponse
import com.example.sebook.data.model.SubmitBookingResponse
import com.example.sebook.data.network.ApiService

class HistoryRepository(private val apiService: ApiService) {

    suspend fun getHistory(): HistoryResponse {
        return apiService.getHistory()
    }

    suspend fun deletePengajuan(idPengajuan: String): SubmitBookingResponse {
        return apiService.deletePengajuan(idPengajuan)
    }
}