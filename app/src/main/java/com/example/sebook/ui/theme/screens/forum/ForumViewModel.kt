package com.example.sebook.ui.theme.screens.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.ForumReview
import com.example.sebook.data.repository.ForumRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ForumViewModel(private val repository: ForumRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ForumReview>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<ForumReview>>> = _uiState

    fun getForumReviews() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getForumReviews()
                val formattedReviews = response.reviews.map {
                    it.copy(created_at = formatDateTime(it.created_at))
                }
                _uiState.value = UiState.Success(formattedReviews)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    private fun formatDateTime(dateTimeString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val date = inputFormat.parse(dateTimeString)
            date?.let {
                outputFormat.format(it)
            } ?: dateTimeString
        } catch (e: Exception) {
            dateTimeString // Return original string if parsing fails
        }
    }
}
