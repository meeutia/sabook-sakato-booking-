package com.example.sebook.ui.theme.screens.informasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.DetailRuanganItem
import com.example.sebook.data.repository.RuanganRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailRuanganViewModel(private val repository: RuanganRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DetailRuanganItem>>(UiState.Idle)
    val uiState: StateFlow<UiState<DetailRuanganItem>> = _uiState

    fun getDetailRuangan(idRuangan: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getDetailRuangan(idRuangan)
                if (response.success) {
                    _uiState.value = UiState.Success(response.detail)
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
}