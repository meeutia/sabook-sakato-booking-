package com.example.sebook.ui.theme.screens.informasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.JadwalResponse
import com.example.sebook.data.model.RuanganItem
import com.example.sebook.data.model.SlotTerisiItem
import com.example.sebook.data.repository.RuanganRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RuanganViewModel(private val repository: RuanganRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<RuanganItem>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<RuanganItem>>> = _uiState

    // Diubah untuk menampung seluruh objek JadwalResponse
    private val _jadwalState = MutableStateFlow<UiState<JadwalResponse>>(UiState.Idle)
    val jadwalState: StateFlow<UiState<JadwalResponse>> = _jadwalState

    private val _slotState = MutableStateFlow<UiState<List<SlotTerisiItem>>>(UiState.Idle)
    val slotState: StateFlow<UiState<List<SlotTerisiItem>>> = _slotState

    init {
        getRuangan()
    }

    fun getRuangan() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getRuangan()
                if (response.success) {
                    _uiState.value = UiState.Success(response.ruangan)
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

    fun getJadwalRuangan(idRuangan: String, idPengajuan: String? = null) {
        _jadwalState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = if (idPengajuan != null) {
                    repository.getJadwalRuanganForEdit(idRuangan, idPengajuan)
                } else {
                    repository.getJadwalRuangan(idRuangan)
                }
                _jadwalState.value = UiState.Success(response)
            } catch (e: HttpException) {
                _jadwalState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _jadwalState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getSlotTerisi(idRuangan: String, tanggalSewa: String, idPengajuan: String? = null) {
        _slotState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getSlotTerisi(idRuangan, tanggalSewa, idPengajuan)
                if (response.success) {
                    _slotState.value = UiState.Success(response.data_slot_terisi)
                } else {
                    _slotState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _slotState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _slotState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}