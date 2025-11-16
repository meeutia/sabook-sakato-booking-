package com.example.sebook.data.repository

import com.example.sebook.data.model.ChangePasswordRequest
import com.example.sebook.data.model.ChangePasswordResponse
import com.example.sebook.data.model.ProfileResponse
import com.example.sebook.data.model.UpdateProfileRequest
import com.example.sebook.data.model.UpdateProfileResponse
import com.example.sebook.data.network.ApiService

class AuthRepository(private val apiService: ApiService) {

    suspend fun changePassword(request: ChangePasswordRequest): ChangePasswordResponse {
        return apiService.changePassword(request)
    }

    suspend fun getProfile(): ProfileResponse {
        return apiService.getProfile()
    }

    suspend fun updateProfile(request: UpdateProfileRequest): UpdateProfileResponse {
        return apiService.updateProfile(request)
    }
}