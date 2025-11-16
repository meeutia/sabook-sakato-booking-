package com.example.sebook.data.repository

import com.example.sebook.data.model.LoginRequest
import com.example.sebook.data.model.LoginResponse
import com.example.sebook.data.network.ApiService

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }
}