package com.example.sebook.data.repository

import com.example.sebook.data.model.RegisterRequest
import com.example.sebook.data.model.RegisterResponse
import com.example.sebook.data.network.ApiService

class RegisterRepository(private val apiService: ApiService) {
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }
}
