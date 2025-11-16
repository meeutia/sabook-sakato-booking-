package com.example.sebook.data.repository

import com.example.sebook.data.model.ReviewRequest
import com.example.sebook.data.model.ReviewResponse
import com.example.sebook.data.network.ApiService

class ReviewRepository(private val apiService: ApiService) {
    suspend fun submitReview(idPengajuan: String, request: ReviewRequest): ReviewResponse {
        return apiService.submitReview(idPengajuan, request)
    }
}