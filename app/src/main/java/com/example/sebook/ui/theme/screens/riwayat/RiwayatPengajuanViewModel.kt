package com.example.sebook.ui.theme.screens.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.HistoryItem
import com.example.sebook.data.model.SubmitBookingResponse
import com.example.sebook.data.repository.HistoryRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RiwayatPengajuanViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<HistoryItem>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<HistoryItem>>> = _uiState

    private val _deleteState = MutableStateFlow<UiState<SubmitBookingResponse>>(UiState.Idle)
    val deleteState: StateFlow<UiState<SubmitBookingResponse>> = _deleteState

    fun getHistory() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getHistory()
                if (response.success) {
                    val sortedList = response.history?.sortedByDescending { it.tanggal_sewa } ?: emptyList()
                    _uiState.value = UiState.Success(sortedList)
                } else {
                    _uiState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun deletePengajuan(idPengajuan: String) {
        _deleteState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.deletePengajuan(idPengajuan)
                if (response.success) {
                    _deleteState.value = UiState.Success(response)
                    // Refresh the history list
                    getHistory()
                } else {
                    _deleteState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _deleteState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _deleteState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}