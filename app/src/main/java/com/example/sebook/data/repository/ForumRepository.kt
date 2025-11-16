
package com.example.sebook.data.repository

import com.example.sebook.data.model.ForumReviewResponse
import com.example.sebook.data.network.ApiService

class ForumRepository(private val apiService: ApiService) {

    suspend fun getForumReviews(): ForumReviewResponse {
        return apiService.getForumReviews()
    }

    companion object {
        @Volatile
        private var instance: ForumRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ForumRepository = instance ?: synchronized(this) {
            instance ?: ForumRepository(apiService)
        }.also { instance = it }
    }
}
