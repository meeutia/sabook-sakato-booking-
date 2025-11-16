package com.example.sebook.ui.theme.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.BarangItem
import com.example.sebook.data.model.BookingRequest
import com.example.sebook.data.repository.CheckBarangRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheckBarangViewModel(private val repository: CheckBarangRepository) : ViewModel() {

    private val _barangState = MutableStateFlow<UiState<List<BarangItem>>>(UiState.Idle)
    val barangState: StateFlow<UiState<List<BarangItem>>> = _barangState

    fun checkAvailableBarang(bookingRequest: BookingRequest) {
        _barangState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.checkAvailableBarang(bookingRequest)
                if (response.success) {
                    _barangState.value = UiState.Success(response.barang)
                } else {
                    _barangState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _barangState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _barangState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
