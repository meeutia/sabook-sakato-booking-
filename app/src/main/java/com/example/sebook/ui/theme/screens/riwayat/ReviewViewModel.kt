package com.example.sebook.ui.theme.screens.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.ReviewRequest
import com.example.sebook.data.model.ReviewResponse
import com.example.sebook.data.repository.ReviewRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ReviewResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<ReviewResponse>> = _uiState

    fun submitReview(idPengajuan: String, review: String, rating: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val request = ReviewRequest(review = review, rating = rating)
                val response = repository.submitReview(idPengajuan, request)
                if (response.success) {
                    _uiState.value = UiState.Success(response)
                } else {
                    _uiState.value = UiState.Error(response.message)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}